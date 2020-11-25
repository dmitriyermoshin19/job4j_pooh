package pooh;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pooh.service.Decryptor;
import pooh.service.PoohJms;
import pooh.store.Storage;
import pooh.store.Store;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PoohJmsTest {

    private final Store<String> store = new Storage();
    private final PoohJms jms = new PoohJms(store);
    private final Decryptor decryptor = mock(Decryptor.class);
    private final Socket socket = mock(Socket.class);

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < 10; i++) {
            store.add("Hello");
        }
    }

    @Test
    public void getSocket() throws IOException {
        ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
        Socket socket = new Socket();
        when(serverSocket.accept()).thenReturn(socket);
        Socket out = new PoohJms(new Storage()).getSocket(serverSocket);
        assertThat(socket, is(out));
    }

    @Test
    public void whenTopicModeExecute() throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thread first = new Thread(() -> {
            when(decryptor.getMode()).thenReturn("TOPIC");
            when(decryptor.getClient()).thenReturn("Subscriber");
            try {
                when(socket.getOutputStream()).thenReturn(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            jms.execute(decryptor, socket);
        });
        Thread second = new Thread(() -> {
            when(decryptor.getMode()).thenReturn("TOPIC");
            when(decryptor.getClient()).thenReturn("Subscriber");
            try {
                when(socket.getOutputStream()).thenReturn(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            jms.execute(decryptor, socket);
        });
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(store.size(), is(10));
    }

    @Test
    public void whenQueueModeExecute() throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thread first = new Thread(() -> {
            when(decryptor.getMode()).thenReturn("queue");
            when(decryptor.getClient()).thenReturn("Subscriber");
            try {
                when(socket.getOutputStream()).thenReturn(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            jms.execute(decryptor, socket);
        });
        Thread second = new Thread(() -> {
            when(decryptor.getMode()).thenReturn("queue");
            when(decryptor.getClient()).thenReturn("Subscriber");
            try {
                when(socket.getOutputStream()).thenReturn(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            jms.execute(decryptor, socket);
        });
        first.start();
        second.start();
        first.join();
        second.join();
        Thread.sleep(500);
        assertThat(store.size(), is(8));
    }
}
