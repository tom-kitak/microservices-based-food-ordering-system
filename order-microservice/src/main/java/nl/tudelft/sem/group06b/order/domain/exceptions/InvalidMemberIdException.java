package nl.tudelft.sem.group06b.order.domain.exceptions;

public class InvalidMemberIdException extends Exception {
    static final long serialVersionUID = -3387516993124129948L;

    public InvalidMemberIdException() {
        super("Invalid member ID");
    }
}
