package nl.tudelft.sem.group06b.order.domain.exceptions;

public class InvalidOrderLocationException extends Exception {
    static final long serialVersionUID = -3387516993124129945L;

    public InvalidOrderLocationException() {
        super("Invalid store location");
    }
}
