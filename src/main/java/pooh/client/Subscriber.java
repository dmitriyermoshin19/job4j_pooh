package pooh.client;

import pooh.store.Store;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Subscriber implements Client {
    private final Socket socket;
    private final Store<String> store;

    public Subscriber(Socket socket, Store<String> store) {
        this.socket = socket;
        this.store = store;
    }

    /**
     * Method getting content and sending it to subscriber
     */
    @Override
    public void doJobs() {
        String content = store.get();
        try (BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
            out.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        doJobs();
    }
}
