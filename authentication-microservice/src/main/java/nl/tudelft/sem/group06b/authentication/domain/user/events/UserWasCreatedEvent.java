package nl.tudelft.sem.group06b.authentication.domain.user.events;

import nl.tudelft.sem.group06b.authentication.domain.user.Username;

/**
 * A DDD domain event that indicated a user was created.
 */
public class UserWasCreatedEvent {
    private final Username username;

    public UserWasCreatedEvent(Username username) {
        this.username = username;
    }

    public Username getUsername() {
        return username;
    }
}
