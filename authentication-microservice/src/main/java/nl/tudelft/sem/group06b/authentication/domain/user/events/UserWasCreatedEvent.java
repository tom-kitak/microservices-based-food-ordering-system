package nl.tudelft.sem.group06b.authentication.domain.user.events;

import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;

/**
 * A DDD domain event that indicated a user was created.
 */
public class UserWasCreatedEvent {
    private final MemberID username;

    public UserWasCreatedEvent(MemberID username) {
        this.username = username;
    }

    public MemberID getUsername() {
        return username;
    }
}
