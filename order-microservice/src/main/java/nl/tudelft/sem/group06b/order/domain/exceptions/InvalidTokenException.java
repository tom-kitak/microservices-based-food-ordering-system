package nl.tudelft.sem.group06b.order.domain.exceptions;

public class InvalidTokenException extends Exception {
    static final long serialVersionUID = -3387516993124129943L;

    public InvalidTokenException() {
        super("No active order with this ID");
    }
}
