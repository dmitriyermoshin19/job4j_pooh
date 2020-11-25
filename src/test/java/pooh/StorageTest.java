package pooh;

import org.junit.Test;
import pooh.store.Storage;
import pooh.store.Store;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class StorageTest {

    @Test
    public void get() throws InterruptedException {
        Store<String> store = new Storage();
        Thread first = new Thread(() -> {
            store.add("Hi there");
            store.add("123");
            store.add("It's i am");
        });
        Thread second = new Thread(() -> {
            store.get();
            store.get();
        });
        first.start();
        second.start();
        first.join();
        second.join();
        String out = store.get();
        boolean result = out.equals("Hi there")
                || out.equals("123")
                || out.equals("It's i am");
        assertThat(result, is(true));
    }
}
