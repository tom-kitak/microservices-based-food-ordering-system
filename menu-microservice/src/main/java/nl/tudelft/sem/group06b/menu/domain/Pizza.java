package nl.tudelft.sem.group06b.menu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private @Getter Long id;
    /**
     * list of toppings that the pizza has.
     */
    @OneToMany
    private @Getter @Setter List<Topping> toppings;
    /**
     * The name of the pizza.
     */
    private @Getter @Setter String name;

    /**
     * The total price of the pizza.
     */
    private @Getter @Setter BigDecimal price;

    /**
     * Constructor for pizza.
     *
     * @param id id of the pizza.
     * @param toppings toppings on the pizza.
     * @param name name for the pizza.
     * @param price price of the pizza.
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
     * @return true if contains allergy, false if it doesn't.
     */
    public boolean containsAllergen(Allergy a) {
        for (Topping t : this.toppings) {
            if (t.containsAllergy(a)) {
                return true;
            }
        }
        return false;
    }
}
