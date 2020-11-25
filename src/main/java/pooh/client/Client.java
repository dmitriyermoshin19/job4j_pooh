package pooh.client;

public interface Client extends Runnable {
    /**
     * Method should execute task in dependency of given client
     */
    void doJobs();
}
