package nl.tudelft.sem.group06b.authentication.domain.role.events;

import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;

/**
 * A DDD domain event that indicated a new role was created.
 */
public class RoleWasCreatedEvent {
    private final RoleName roleName;

    public RoleWasCreatedEvent(RoleName roleName) {
        this.roleName = roleName;
    }

    public RoleName getRoleName() {
        return roleName;
    }
}