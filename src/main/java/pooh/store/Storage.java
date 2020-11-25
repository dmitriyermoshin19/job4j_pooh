package pooh.store;

import net.jcip.annotations.ThreadSafe;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ThreadSafe
public class Storage implements Store<String> {
    private final Queue<String> store = new ConcurrentLinkedQueue<>();

    @Override
    public String get() {
        return store.poll();
    }

    @Override
    public void add(String some) {
        store.offer(some);
    }

    @Override
    public Store<String> copyStore() {
        Storage newStore = new Storage();
        for (String s : store) {
            newStore.add(s);
        }
        return newStore;
    }

    @Override
    public int size() {
        return store.size();
    }
}
