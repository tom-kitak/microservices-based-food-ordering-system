package nl.tudelft.sem.group06b.user.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


/**
 * A DDD service for registering a new user.
 */
@Service
public class UserService {
    private final transient UserRepository userRepository;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository  the user repository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Register a new user.
     *
     * @param memberId    The memberId of the user
     * @throws Exception if the user already exists
     */
    public User addUser(String memberId, Role role, List<Allergy> allergies, Location preferredLocation) throws Exception {

        if (checkMemberIdIsUnique(memberId)) {
            // Create new account
            User user = new User(memberId, role, allergies, preferredLocation);
            userRepository.save(user);

            return user;
        }

        throw new Exception("MemberId is already in use");
    }

    /**
     * Register a new user.
     *
     * @param memberId    The memberId of the user
     * @throws Exception if the user already exists
     */
    public User getUser(String memberId) throws Exception {

        Optional<User> user = userRepository.findByMemberId(memberId);

        if (!checkMemberIdIsUnique(memberId) && user.isPresent()) {
            // Get existing user
            return user.get();
        }

        throw new Exception("MemberId does not exist");
    }

    public boolean checkMemberIdIsUnique(String memberId) {
        return !userRepository.existsByMemberId(memberId);
    }
}
