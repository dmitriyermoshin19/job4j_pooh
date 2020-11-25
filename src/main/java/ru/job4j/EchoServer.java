package ru.job4j;

import java.io.IOException;
import java.net.ServerSocket;

public class EchoServer {
    private final ServerSocket server;
    private final ExecutorPool exPool;

    public EchoServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.exPool = new ExecutorPool();
    }

    public void start() {
        while (!server.isClosed()) {
                exPool.execute(() -> {
                    try {
                        new Handler(server, exPool).run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }
}
