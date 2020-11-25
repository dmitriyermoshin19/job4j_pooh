package pooh;

import org.junit.Test;
import org.mockito.Mockito;
import pooh.client.Subscriber;
import pooh.store.Storage;
import pooh.store.Store;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class SubscriberTest {

    @Test
    public void doJobs() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Socket socket = Mockito.mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(out);
        Store<String> store = new Storage();
        store.add("Hello");
        Subscriber subscriber = new Subscriber(socket, store);
        subscriber.doJobs();
        assertThat(out.toString(), is("Hello"));
    }
}
