package pooh.service;

import pooh.store.Storage;
import pooh.store.Store;
import pooh.client.Client;
import pooh.client.Publisher;
import pooh.client.Subscriber;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoohJms implements Service<Socket, ServerSocket> {
    private final ExecutorService service = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Store<String> store;

    public PoohJms(Store<String> store) {
        this.store = store;
    }

    /**
     * Method returns socket from client
     *
     * @param server
     * @return socket
     */
    @Override
    public Socket getSocket(ServerSocket server) {
        Socket socket = null;
        try {
            socket = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    /**
     * Method returns Sender by given mode from request
     *
     * @param decryptor
     * @param socket
     * @return Sender
     */
    public Client getClientByMode(Decryptor decryptor, Socket socket) {
        if (decryptor.getMode().equals("queue")
                && decryptor.getClient().equals("Subscriber")) {
            return new Subscriber(socket, store);
        }
        if (decryptor.getMode().equals("TOPIC")
                && decryptor.getClient().equals("Subscriber")) {
            return new Subscriber(socket, store.copyStore());
        }
        return new Publisher(store, decryptor.getJson());
    }

    /**
     * Method execute tasks
     *
     * @param decryptor
     * @param socket
     */
    @Override
    public void execute(Decryptor decryptor, Socket socket) {
        Client client = this.getClientByMode(decryptor, socket);
        service.submit(client);
    }

    public static void main(String[] args) throws IOException {
        Service<Socket, ServerSocket> poo = new PoohJms(new Storage());
        ServerSocket server = new ServerSocket(9000, 0, InetAddress.getLocalHost());
        while (!server.isClosed()) {
            Socket socket = poo.getSocket(server);
            socket.getOutputStream().write(("GET /ip HTTP/1.0\n"
                    .getBytes(StandardCharsets.US_ASCII)));
            socket.getOutputStream().flush();
            Decryptor decryptor = new Decryptor(socket);
            poo.execute(decryptor, socket);
        }
    }
}
