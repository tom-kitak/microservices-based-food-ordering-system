package nl.tudelft.sem.template.example.domain;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    /**
     * Class representing the user entity
     */

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Id
    @Column(name = "memberId", nullable = false, unique = true)
    private String memberId;

    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "allergies")
    @ElementCollection
    private List<Allergy> allergies;

    @Column(name = "location")
    private Location preferredLocation;

    /**
     * Instantiates a new User
     * @param memberId      the username of the user
     * @param role          the role of the user
     * @param allergies     the list of allergies of the user
     */
    public User(String memberId, Role role, List<Allergy>allergies, Location preferredLocation){
        this.memberId = memberId;
        this.role = role;
        this.allergies = allergies;
        this.preferredLocation = preferredLocation;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
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

    public Location getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(Location preferredLocation) {
        this.preferredLocation = preferredLocation;
    }
}
