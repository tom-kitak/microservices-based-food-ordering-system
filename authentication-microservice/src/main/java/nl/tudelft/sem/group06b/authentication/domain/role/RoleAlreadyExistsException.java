package nl.tudelft.sem.group06b.authentication.domain.role;

import nl.tudelft.sem.group06b.authentication.domain.user.Username;

/**
 * Exception to indicate the role was already created
 */
public class RoleAlreadyExistsException extends Exception {
    static final long serialVersionUID = -3387516993124229945L;

    public RoleAlreadyExistsException(String roleName) {
        super(roleName);
    }
}