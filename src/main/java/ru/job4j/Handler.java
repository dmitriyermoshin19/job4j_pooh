package ru.job4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses requests from client and create tasks for {@link ExecutorPool}.
 */
public class Handler {
    private final Pattern postQueue = Pattern.compile("^(POST /queue/)(.+)");
    private final Pattern postTopic = Pattern.compile("^(POST /topic/)(.+)");
    private final ServerSocket server;
    private final Socket socket;
    private final ExecutorPool exPool;

    public Handler(ServerSocket server, ExecutorPool exPool) throws IOException {
        this.server = server;
        this.socket = server.accept();
        this.exPool = exPool;
    }

    /**
     * Creates {@link ConnectionIO} object for parse requests
     * and transfer them to executing to {@link Handler#exPool}.
     *  Request "-1" - closes server.
     *  Request "exit" - closes current connection.
     */
    public void run() {
        try (ConnectionIO conIO = new ConnectionIO(socket)) {
            exPool.add(conIO);
            String request;

            while (!socket.isClosed()
                    && (request = conIO.readLine()) != null
                    && !request.equals("exit")) {

                Matcher mpQ = postQueue.matcher(request);
                Matcher mpT = postTopic.matcher(request);

                if (mpQ.matches()) {
                    exPool.offerQueue(mpQ.group(2));
                } else if (request.equals("GET /queue")) {
                    exPool.getQueue(conIO);
                } else if (mpT.matches()) {
                    exPool.offerTopic(mpT.group(2));
                } else if (request.equals("GET /topic")) {
                    exPool.getTopic(conIO);
                } else if (request.equals("-1")) {
                    socket.close();
                    exPool.shutdown();
                    server.close();
                }
            }
            exPool.remove(conIO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
