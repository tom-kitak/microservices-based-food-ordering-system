package nl.tudelft.sem.group06b.authentication.domain.user;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing a username in our domain.
 */
@EqualsAndHashCode
public class Username {
    private final transient String usernameValue;

    public Username(String username) {
        // validate username
        this.usernameValue = username;
    }

    @Override
    public String toString() {
        return usernameValue;
    }
}
