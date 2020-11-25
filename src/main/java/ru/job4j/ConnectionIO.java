package ru.job4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionIO implements Closeable {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final ConcurrentLinkedQueue<Item> itemQueueTopic = new ConcurrentLinkedQueue<>();

    public ConnectionIO(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.reader = createReader();
        this.writer = createWriter();
    }

    public ConnectionIO(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = createReader();
        this.writer = createWriter();
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(
                new InputStreamReader(this.socket.getInputStream()));
    }

    private BufferedWriter createWriter() throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void writeLine(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }

    public void topicOffer(Item i) {
        itemQueueTopic.offer(i.copyOf());
    }

    public Item topicPoll() {
        return itemQueueTopic.poll();
    }
}
