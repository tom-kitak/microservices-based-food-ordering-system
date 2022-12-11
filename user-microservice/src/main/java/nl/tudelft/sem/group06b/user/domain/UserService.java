package nl.tudelft.sem.group06b.user.domain;

import java.util.ArrayList;
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

    private final transient String nonexistentMemberId = "MemberId does not exist";

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
    public User addUser(String memberId, List<Allergy> allergies, Location preferredLocation) throws Exception {

        if (checkMemberIdIsUnique(memberId)) {
            // Create new account
            User user = new User(memberId, allergies, preferredLocation);
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

        throw new Exception(nonexistentMemberId);
    }

    /**
     * Add a new allergy.
     *
     * @param memberId  memberId of user
     * @param allergy   allergen to be added to the user's list of allergies
     * @return          the updated User
     * @throws Exception if the allergen is already in the list or if the memberId is not present
     */
    public User addAllergy(String memberId, Allergy allergy) throws Exception {
        Optional<User> user = userRepository.findByMemberId(memberId);
        if (user.isPresent()) {
            List<Allergy> allergies = user.get().getAllergies();
            if (allergies.stream().map(Allergy::getAllergen).collect(Collectors.toList())
                    .contains(allergy.getAllergen())) {
                throw new Exception("Allergy is already there");
            }
            List<Allergy> newAllergies = new ArrayList<>(allergies);
            newAllergies.add(allergy);
            user.get().setAllergies(newAllergies);
            userRepository.save(user.get());
            return user.get();
        }
        throw new Exception(nonexistentMemberId);
    }

    /**
     * Remove an allergy.
     *
     * @param memberId  memberId of user
     * @param allergy   allergen to be removed from the user's list of allergies
     * @return          the updated User
     * @throws Exception if the allergen is not in the list or if the memberId is not present
     */
    public User removeAllergy(String memberId, Allergy allergy) throws Exception {
        Optional<User> user = userRepository.findByMemberId(memberId);
        if (user.isPresent()) {
            List<Allergy> allergies = user.get().getAllergies();
            if (!allergies.stream().map(Allergy::getAllergen).collect(Collectors.toList())
                    .contains(allergy.getAllergen())) {
                throw new Exception("Allergy is not there");
            }
            user.get().setAllergies(allergies.stream()
                    .filter(x -> !x.getAllergen().equals(allergy.getAllergen()))
                    .collect(Collectors.toList()));
            userRepository.save(user.get());
            return user.get();
        }
        throw new Exception(nonexistentMemberId);
    }

    /**
     * Remove an allergy.
     *
     * @param memberId  memberId of user
     * @return          the updated User
     * @throws Exception if the memberId is not present
     */
    public User removeAllAllergies(String memberId) throws Exception {
        Optional<User> user = userRepository.findByMemberId(memberId);
        if (user.isPresent()) {
            user.get().setAllergies(new ArrayList<Allergy>());
            userRepository.save(user.get());
            return user.get();
        }
        throw new Exception(nonexistentMemberId);
    }



    public boolean checkMemberIdIsUnique(String memberId) {
        return !userRepository.existsByMemberId(memberId);
    }
}
