package pooh.service;

import pooh.client.Client;

public interface Service<T, V> {

    /**
     * Method should return request from sender
     *
     * @param server
     * @return T
     */
    T getSocket(V server);

    /**
     * Method should do main logic
     *
     * @param decryptor
     * @param from
     */
    void execute(Decryptor decryptor, T from);

    /**
     * Method should return sender in dependency on mode
     *
     * @param decryption
     * @param to
     * @return Sender
     */
    Client getClientByMode(Decryptor decryption, T to);
}
