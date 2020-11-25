package pooh.client;

import pooh.store.Store;

public class Publisher implements Client {
    private final Store<String> store;
    private final String json;

    public Publisher(Store<String> store, String json) {
        this.store = store;
        this.json = json;
    }

    @Override
    public void doJobs() {
        store.add(json);
    }

    @Override
    public void run() {
        doJobs();
    }
}
