package nl.tudelft.sem.group06b.order.domain.exceptions;

public class InvalidOrderContentsException extends Exception {
    static final long serialVersionUID = -3387516993124129947L;

    public InvalidOrderContentsException() {
        super("Invalid order contents");
    }
}
