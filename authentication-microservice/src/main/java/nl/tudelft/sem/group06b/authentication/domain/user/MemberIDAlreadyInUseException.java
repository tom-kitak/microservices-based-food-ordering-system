package nl.tudelft.sem.group06b.authentication.domain.user;

/**
 * Exception to indicate the username is already in use.
 */
public class MemberIDAlreadyInUseException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public MemberIDAlreadyInUseException(MemberID username) {
        super(username.toString());
    }
}
