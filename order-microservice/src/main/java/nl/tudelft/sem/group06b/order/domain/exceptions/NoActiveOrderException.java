package nl.tudelft.sem.group06b.order.domain.exceptions;

public class NoActiveOrderException extends Exception {
    static final long serialVersionUID = -3387516993124129942L;

    public NoActiveOrderException() {
        super("Invalid token");
    }
}
