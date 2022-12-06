<<<<<<<< HEAD:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/domain/user/RegistrationService.java
package nl.tudelft.sem.group06b.authentication.domain.user;
========
package nl.tudelft.sem.group06b.authentication.domain.user.service;
>>>>>>>> dev:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/domain/user/service/RegistrationService.java

import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import nl.tudelft.sem.group06b.authentication.domain.user.UsernameAlreadyInUseException;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */
@Service
public class RegistrationService {
    private final transient UserRepository userRepository;
    private final transient PasswordHashingService passwordHashingService;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository         the user repository
     * @param passwordHashingService the password encoder
     */
    public RegistrationService(UserRepository userRepository, PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    /**
     * Register a new user.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @throws Exception if the user already exists
     */
    public User registerUser(Username username, Password password) throws Exception {

        if (checkUsernameIsUnique(username)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            User user = new User(username, hashedPassword, 2L);
            userRepository.save(user);

            return user;
        }

        throw new UsernameAlreadyInUseException(username);
    }

    public boolean checkUsernameIsUnique(Username username) {
        return !userRepository.existsByUsername(username);
    }
}
