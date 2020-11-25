package ru.job4j;

import java.util.concurrent.*;

/**
 * Pool of threads, which working with tasks, which sent from {@link Handler}.
 * {@link ExecutorPool#exService} for creating threads and allocating tasks between threads.
 * {@link ExecutorPool#queueItem} queue for storing items.
 * {@link ExecutorPool#conIOQueue} store for connections to {@link EchoServer}.
 */
public class ExecutorPool {
    private final ExecutorService exService;
    private final ConcurrentLinkedQueue<Item> queueItem;
    private final ConcurrentLinkedQueue<ConnectionIO> conIOQueue;

    public ExecutorPool() {
        this.exService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.queueItem = new ConcurrentLinkedQueue<>();
        this.conIOQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Executes {@link Runnable} tasks.
     *
     * @param r Runnable object.
     */
    protected void execute(Runnable r) {
        exService.submit(r);
    }

    /**
     * Creates object by request and add it to {@link ExecutorPool#queueItem}.
     *
     * @param post request.
     */
    protected void offerQueue(String post) {
        exService.submit(() -> queueItem.offer(new Item(post)));
    }

    /**
     * Return to client the first element of {@link ExecutorPool#queueItem}.
     *
     * @param conn {@link ConnectionIO} object, which created while client connected to
     * {@link EchoServer}.
     */
    protected void getQueue(ConnectionIO conn) {
        Future<Item> futureItem = exService.submit(queueItem::poll);
        try {
            if (futureItem.get() != null) {
                conn.writeLine(futureItem.get().getPost());
            } else {
                conn.writeLine("queue is empty");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add connection to {@link ExecutorPool#conIOQueue}.
     */
    protected void add(ConnectionIO conIO) {
        conIOQueue.add(conIO);
    }

    /**
     * @param post create object by request and add
     * to {@link ConnectionIO}#itemQueueTopic
     * each of {@link ExecutorPool#conIOQueue} for each recipient.
     */
    protected void offerTopic(String post) {
        exService.submit(() -> {
            for (ConnectionIO c : conIOQueue) {
                c.topicOffer(new Item(post));
            }
        });
    }

    /**
     * Return to client the last element of {@link ConnectionIO}#itemQueueTopic.
     * If topic is empty return message.
     *
     * @param con {@link ConnectionIO} object, which created while client connected to
     * {@link EchoServer}.
     */
    protected void getTopic(ConnectionIO con) {
        Future<Item> future = exService.submit(con::topicPoll);
        try {
            if (future.get() != null) {
                con.writeLine(future.get().getPost());
            } else {
                con.writeLine("topic is empty");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes connection from {@link ExecutorPool#conIOQueue}.
     *
     * @param conn ConnectionIO object, which created while client connected to server.
     */
    protected void remove(ConnectionIO conn) {
        conIOQueue.remove(conn);
    }

    /**
     * Clear connections list and shutdowns {@link ExecutorPool#exService}.
     */
    protected void shutdown() {
        conIOQueue.clear();
        exService.shutdownNow();
    }
}
