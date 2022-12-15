package nl.tudelft.sem.group06b.menu.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Entity
@Table(name = "Allergies")
public class Allergy implements Serializable {
    static final long serialVersionUID = 42L;
    /**
     * id for the allergy.
     */
    @Id
    private @Getter Long id;
    /**
     * name for the allergy.
     */
    private @Getter String name;

    public Allergy(long id, String allergen) {
        this.id = id;
        this.name = allergen;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Allergy) {
            Allergy that = (Allergy) other;
            return this.getId().equals(that.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
