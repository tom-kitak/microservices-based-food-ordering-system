<<<<<<<< HEAD:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/domain/user/PasswordHashingService.java
package nl.tudelft.sem.group06b.authentication.domain.user;
========
package nl.tudelft.sem.group06b.authentication.domain.user.service;
>>>>>>>> dev:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/domain/user/service/PasswordHashingService.java

import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A DDD service for hashing passwords.
 */
public class PasswordHashingService {

    private final transient PasswordEncoder encoder;

    public PasswordHashingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public HashedPassword hash(Password password) {
        return new HashedPassword(encoder.encode(password.toString()));
    }
}

