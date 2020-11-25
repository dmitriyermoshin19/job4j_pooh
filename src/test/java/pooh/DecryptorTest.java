package pooh;

import org.junit.Test;
import org.mockito.Mockito;
import pooh.service.Decryptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class DecryptorTest {

    private final Socket socket = Mockito.mock(Socket.class);

    @Test
    public void getDecryptorTest() throws IOException {
        when(socket.getInputStream())
                .thenReturn(new ByteArrayInputStream("POST/queue/{\"queue\" : \"weather\", \"text\" : \"temperature +18 C\"}".getBytes()));
        Decryptor decryptor = new Decryptor(socket);
        String json = decryptor.getJson();
        String mode = decryptor.getMode();
        String client = decryptor.getClient();
        assertThat(json, is("{\"queue\" : \"weather\", \"text\" : \"temperature +18 C\"}"));
        assertThat(mode, is("queue"));
        assertThat(client, is("Publisher"));
    }
}