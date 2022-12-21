package nl.tudelft.sem.group06b.store.domain;

public class NoSuchStoreException extends Exception {

    static final long serialVersionUID = -435357243186498124L;

    /**
     * Indicates the store does not exist.
     */
    public NoSuchStoreException() {
        super("Store does not exist");
    }
}
