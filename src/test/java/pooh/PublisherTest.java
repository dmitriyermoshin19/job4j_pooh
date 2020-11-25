package pooh;

import org.junit.Test;
import pooh.client.Publisher;
import pooh.store.Storage;
import pooh.store.Store;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class PublisherTest {

    @Test
    public void doJobs() {
        Store<String> store = new Storage();
        Publisher publisher = new Publisher(store, "Hi all");
        publisher.doJobs();
        assertThat(store.get(), is("Hi all"));
    }
}
