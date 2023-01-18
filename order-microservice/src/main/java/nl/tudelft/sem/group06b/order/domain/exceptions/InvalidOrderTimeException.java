package nl.tudelft.sem.group06b.order.domain.exceptions;

public class InvalidOrderTimeException extends Exception {
    static final long serialVersionUID = -3387516993124129944L;

    public InvalidOrderTimeException() {
        super("Invalid time. The time should be at least 30 minutes after the current time");
    }
}
