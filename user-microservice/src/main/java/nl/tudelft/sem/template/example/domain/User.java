package nl.tudelft.sem.template.example.domain;
import java.util.List;

public class User {
    /**
     * Class representing the user entity
     */

    private Role role;
    private List<Allergy> allergies;

    /**
     * Instantiates a new User
     * @param role          the role of the user
     * @param allergies     the list of allergies of the user
     */
    public User(Role role,List<Allergy>allergies){
        this.role = role;
        this.allergies = allergies;
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
