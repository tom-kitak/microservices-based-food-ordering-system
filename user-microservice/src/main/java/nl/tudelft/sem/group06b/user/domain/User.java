package nl.tudelft.sem.group06b.user.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    /**
     * Class representing the user entity.
     */

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "memberId", nullable = false, unique = true)
    private String memberId;

    @Column(name = "allergies")
    @ElementCollection
    private List<Allergy> allergies;

    @Column(name = "location")
    private Location preferredLocation;

    /**
     * Instantiates a new User.
     *
     * @param memberId          the username of the user.
     * @param allergies         the list of allergies of the user.
     * @param preferredLocation the preferred store location of the user.
     *
     */
    public User(String memberId, List<Allergy> allergies, Location preferredLocation) {
        this.memberId = memberId;
        this.allergies = allergies;
        this.preferredLocation = preferredLocation;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id && Objects.equals(memberId, user.memberId)
                && Objects.equals(allergies, user.allergies)
                && Objects.equals(preferredLocation, user.preferredLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, allergies, preferredLocation);
    }
}
