package pooh.store;

public interface Store<T> {
    /**
     * Method return something from store
     *
     * @return T
     */
    T get();

    /**
     * Method add something to store
     *
     * @param some
     */
    void add(T some);

    /**
     * Method of copy store
     *
     * @return
     */
    Store<T> copyStore();

    /**
     * Return size
     *
     * @return size
     */
    int size();
}
