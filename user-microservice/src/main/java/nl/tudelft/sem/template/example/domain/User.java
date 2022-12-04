package nl.tudelft.sem.template.example.domain;
import java.util.List;

public class User {
    /**
     * Class representing the user entity
     */

    private int memberId;
    private Role role;
    private List<Allergy> allergies;

    /**
     * Instantiates a new User
     * @param memberId      the memberId of the user
     * @param role          the role of the user
     * @param allergies     the list of allergies of the user
     */
    public User(int memberId, Role role, List<Allergy>allergies){
        this.memberId = memberId;
        this.role = role;
        this.allergies = allergies;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }
}
