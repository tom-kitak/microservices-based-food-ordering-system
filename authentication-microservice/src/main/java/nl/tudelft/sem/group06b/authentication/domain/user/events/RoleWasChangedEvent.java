package nl.tudelft.sem.group06b.authentication.domain.user.events;

import nl.tudelft.sem.group06b.authentication.domain.user.User;

/**
 * A DDD domain event indicating a role had changed.
 */
public class RoleWasChangedEvent {
    private final User user;

    public RoleWasChangedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

