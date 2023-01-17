package nl.tudelft.sem.group06b.order.domain.exceptions;

public class InvalidOrderIdException extends Exception {
    static final long serialVersionUID = -3387516993124129946L;

    public InvalidOrderIdException() {
        super("Invalid order ID");
    }
}
