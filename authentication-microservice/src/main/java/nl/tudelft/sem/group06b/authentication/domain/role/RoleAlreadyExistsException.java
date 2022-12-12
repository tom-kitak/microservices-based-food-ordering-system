package nl.tudelft.sem.group06b.authentication.domain.role;

/**
 * Exception to indicate the role was already created.
 */
public class RoleAlreadyExistsException extends Exception {
    static final long serialVersionUID = -3387516993124229945L;

    public RoleAlreadyExistsException(RoleName roleName) {
        super(roleName.getRoleNameValue());
    }
}