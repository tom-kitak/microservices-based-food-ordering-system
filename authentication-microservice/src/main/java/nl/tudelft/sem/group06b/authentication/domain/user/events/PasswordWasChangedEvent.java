package nl.tudelft.sem.group06b.authentication.domain.user.events;

import nl.tudelft.sem.group06b.authentication.domain.user.User;

/**
 * A DDD domain event indicating a password had changed.
 */
public class PasswordWasChangedEvent {
    private final User user;

    public PasswordWasChangedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

