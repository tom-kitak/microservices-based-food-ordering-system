package nl.tudelft.sem.group06b.authentication.domain.user;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing a password in our domain.
 */
@EqualsAndHashCode
public class Password {
    private final transient String passwordValue;

    public Password(String password) {
        // Validate input
        this.passwordValue = password;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    @Override
    public String toString() {
        return passwordValue;
    }
}
