package nl.tudelft.sem.group06b.menu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Entity
@Table(name = "Toppings")
@ToString
public class Topping implements Serializable {
    static final long serialVersionUID = 42L;
    /**
     * name of the topping.
     */
    private @Getter String name;
    /**
     * id of the allergy.
     */
    @Id
    @GeneratedValue
    private @Getter Long id;
    /**
     * list of the allergies it contains.
     */
    @OneToMany
    private @Getter @Setter List<Allergy> allergies;

    private @Getter @Setter BigDecimal price;

    /**
     * constructor for toppings.
     *
     * @param name the name of the topping.
     * @param allergies the list of allergies for the topping.
     * @param price the price of the topping.
     */
    public Topping(Long id, String name, List<Allergy> allergies, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.allergies = allergies;
        this.price = price;
    }

    /**
     * checks if a topping has a given allergen.
     *
     * @param a the allergen to test against.
     * @return true if it does contain the allergen. False if it doesn't.
     */
    public boolean containsAllergy(Allergy a) {
        for (Allergy allergy : this.getAllergies()) {
            if (a.getId().equals(allergy.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if two Toppings have the same ids.
     *
     * @param obj the topping to check.
     * @return true if they have the same id/false if they don't.
     */
    public boolean hasSameIds(Object obj) {
        if (!(obj instanceof Topping)) {
            return false;
        }
        Topping that = (Topping) obj;
        if (!this.getId().equals(that.getId())) {
            return false;
        }
        Set<Long> ids = this.getAllergies().stream().map(Allergy::getId).collect(Collectors.toSet());
        for (Allergy a : that.getAllergies()) {
            if (!ids.contains(a.getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Topping)) {
            return false;
        }
        Topping that = (Topping) obj;
        return this.getName().equalsIgnoreCase(that.getName())
                && this.getPrice().equals(that.getPrice())
                && this.getAllergies().equals(that.getAllergies());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
