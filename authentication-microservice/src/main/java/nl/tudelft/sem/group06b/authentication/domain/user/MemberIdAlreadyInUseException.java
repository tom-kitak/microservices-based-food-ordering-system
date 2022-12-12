package nl.tudelft.sem.group06b.authentication.domain.user;

/**
 * Exception to indicate the username is already in use.
 */
public class MemberIdAlreadyInUseException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public MemberIdAlreadyInUseException(MemberId username) {
        super(username.toString());
    }
}
