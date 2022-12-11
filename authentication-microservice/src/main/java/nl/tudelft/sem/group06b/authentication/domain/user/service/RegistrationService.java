
package nl.tudelft.sem.group06b.authentication.domain.user.service;


import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberIDAlreadyInUseException;
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
     * @param memberID The memberID of the user
     * @param password The password of the user
     * @throws Exception if the user already exists
     */
    public User registerUser(MemberID memberID, Password password) throws Exception {

        if (checkMemberIDIsUnique(memberID)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            User user = new User(memberID, hashedPassword, 2L);
            userRepository.save(user);

            return user;
        }

        throw new MemberIDAlreadyInUseException(memberID);
    }

    public boolean checkMemberIDIsUnique(MemberID memberID) {
        return !userRepository.existsByMemberID(memberID);
    }
}
