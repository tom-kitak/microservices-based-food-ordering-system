package nl.tudelft.sem.group06b.menu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Entity
@Table(name = "Pizzas")
public class Pizza implements Serializable {
    static final long serialVersionUID = 42L;
    /**
     * id for the pizza.
     */
    @Id
    @Column(name = "id")
    private @Getter Long id;
    /**
     * list of toppings that the pizza has.
     */
    @ManyToMany
    @Column(name = "toppings", unique = false)
    private @Getter @Setter List<Topping> toppings;
    /**
     * The name of the pizza.
     */
    @Column(name = "name")
    private @Getter @Setter String name;

    /**
     * The total price of the pizza.
     */
    @Column(name = "price")
    private @Getter @Setter BigDecimal price;

    /**
     * Constructor for pizza.
     *
     * @param toppings toppings on the pizza.
     * @param name     name for the pizza.
     * @param price    price of the pizza.
     */
    public Pizza(Long id, List<Topping> toppings, String name, BigDecimal price) {
        this.id = id;
        this.toppings = toppings;
        this.name = name;
        this.price = price;
    }

    /**
     * checks if the pizza contains a given allergen.
     *
     * @param a the allergen to test against.
     * @return Optional formatted string/empty if no allergy.
     */
    public Optional<String> containsAllergen(Allergy a) {
        for (Topping t : this.toppings) {
            if (t.containsAllergy(a).isPresent()) {
                return Optional.of(t.containsAllergy(a).get() + "," + this.getId());
            }
        }
        return Optional.empty();
    }

    /**
     * checks if two pizzas and their toppings have the same ids.
     *
     * @param that pizza to check
     * @return true if they have the same ids/false if they don't
     */
    public boolean hasSameIds(Pizza that) {

        HashMap<Long, Topping> toppingHashMap = new HashMap<>();
        if (!this.getId().equals(that.getId())) {
            return false;
        }
        for (Topping t : this.getToppings()) {
            toppingHashMap.put(t.getId(), t);
        }
        Set<Long> toppingIds = toppingHashMap.keySet();
        for (Topping t : that.getToppings()) {
            if (!toppingIds.contains(t.getId())) {
                return false;
            }
            if (!t.hasSameIds(toppingHashMap.get(t.getId()))) {
                return false;
            }
        }
        if (this.getToppings().size() != that.getToppings().size()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Pizza)) {
            return false;
        }
        Pizza that = (Pizza) other;
        return this.name.equals(that.getName())
                && this.price.equals(that.getPrice())
                && this.toppings.equals(that.getToppings());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}


