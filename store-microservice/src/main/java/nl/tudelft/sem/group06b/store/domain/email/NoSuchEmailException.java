package nl.tudelft.sem.group06b.store.domain.email;

public class NoSuchEmailException extends Exception {

    static final long serialVersionUID = -24935969027340937L;

    /**
     * Indicates that the email does not exist.
     */
    public NoSuchEmailException() {
        super("The email does not exist!");
    }
}
