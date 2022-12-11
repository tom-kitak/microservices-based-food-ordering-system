package nl.tudelft.sem.group06b.authentication.domain.user.events;

import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;

/**
 * A DDD domain event that indicated a user was created.
 */
public class UserWasCreatedEvent {
    private final MemberId memberId;

    public UserWasCreatedEvent(MemberId memberId) {
        this.memberId = memberId;
    }

    public MemberId getMemberId() {
        return memberId;
    }
}
