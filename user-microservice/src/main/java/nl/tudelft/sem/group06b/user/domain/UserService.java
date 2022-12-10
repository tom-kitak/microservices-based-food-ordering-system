package nl.tudelft.sem.group06b.user.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    /**
     * Add a new allergy.
     *
     * @param memberId  memberId of user
     * @param allergy   allergen to be added to the user's list of allergies
     * @return          the updated User
     * @throws Exception if the allergen is already in the list
     */
    public User addAllergy(String memberId, Allergy allergy) throws Exception {
        Optional<User> user = userRepository.findByMemberId(memberId);
        if (user.isPresent()) {
            // Get existing user
            List<Allergy> allergies = user.get().getAllergies();
            if (allergies.stream().map(Allergy::getAllergen).collect(Collectors.toList())
                    .contains(allergy.getAllergen())) {
                throw new Exception("Allergy is already there");
            }
            allergies.add(allergy);
            user.get().setAllergies(allergies);
            userRepository.save(user.get());
            return user.get();
        }
        throw new Exception("MemberId does not exist");
    }



    public boolean checkMemberIdIsUnique(String memberId) {
        return !userRepository.existsByMemberId(memberId);
    }
}
