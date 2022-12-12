package nl.tudelft.sem.group06b.store.domain;

public class NoSuchStoreException extends Exception {

    static final long serialVersionUID = -435357243186498124L;

    public NoSuchStoreException(Location location) {
        super("There is no store at " + location.getAddress());
    }
}
