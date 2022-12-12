package nl.tudelft.sem.group06b.authentication.domain.user.service;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A DDD service for hashing passwords.
 */
@AllArgsConstructor
public class PasswordHashingService {

    private final transient PasswordEncoder encoder;

    public HashedPassword hash(Password password) {
        return new HashedPassword(encoder.encode(password.toString()));
    }
}

