package nl.tudelft.sem.group06b.authentication.domain.role.events;

/**
 * A DDD domain event that indicated a new role was created.
 */
public class RoleWasCreatedEvent {
    private final String roleName;

    public RoleWasCreatedEvent(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}