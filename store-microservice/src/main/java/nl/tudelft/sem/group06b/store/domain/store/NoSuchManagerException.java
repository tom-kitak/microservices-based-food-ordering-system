package nl.tudelft.sem.group06b.store.domain.store;

public class NoSuchManagerException extends Exception {

    static final long serialVersionUID = -1245467343141241L;

    public NoSuchManagerException() {
        super("Manager does not exist");
    }
}
