package pooh.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Decryptor {
    private final Socket socket;
    private final String[] splitLine;
    private final String client;
    private final String mode;
    private final String json;

    public Decryptor(Socket socket) {
        this.socket = socket;
        String content = getContentSocket();
        this.splitLine = content.split("/");
        this.json = getJson();
        this.mode = getMode();
        this.client = getClient();
    }

    private String getContentSocket() {
        StringBuilder tmp = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines()
                    .forEach(tmp::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmp.toString();
    }

    public String getClient() {
        if (client == null) {
            return splitLine[0].equals("POST") ? "Publisher" : "Subscriber";
        }
        return client;
    }

    public String getMode() {
        if (mode == null) {
            return splitLine[1];
        }
        return mode;
    }

    public String getJson() {
        if (json == null) {
            return splitLine[2];
        }
        return json;
    }
}
