package nl.tudelft.sem.group06b.order.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Pizza implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    private transient String nameId;

    private transient List<Topping> toppings;

    private transient Set<String> allergenIds;

    private transient BigDecimal price;

    /**
     * Instantiates a new Pizza. Allergens from toppings are automatically added to allergens set.
     *
     * @param nameId name or type of the pizza, serves as an ID
     * @param toppings list of all toppings
     * @param allergenIds set of all allergens
     * @param price price
     */
    public Pizza(String nameId, List<Topping> toppings, Set<String> allergenIds, BigDecimal price) {
        this.nameId = nameId;
        this.toppings = toppings;
        this.allergenIds = allergenIds;
        this.price = price;
        for (Topping t : toppings) {
            for (String allergen : t.getAllergenIds()) {
                allergenIds.add(allergen);
            }
        }
    }

    /**
     * Adds list of toppings to the pizza and also adds the allergies.
     *
     * @param toppings list of toppings to add to the pizza
     */
    public void addToppings(List<Topping> toppings) {
        this.toppings.addAll(toppings);
        for (Topping t : toppings) {
            for (String allergen : t.getAllergenIds()) {
                allergenIds.add(allergen);
            }
        }
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public Set<String> getAllergenIds() {
        return allergenIds;
    }

    public void setAllergenIds(Set<String> allergenIds) {
        this.allergenIds = allergenIds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pizza)) {
            return false;
        }
        Pizza pizza = (Pizza) o;
        return Objects.equals(nameId, pizza.nameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameId);
    }
}
