package nl.tudelft.sem.template.order.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class Pizza {

    private List<Topping> toppings;

    private Set<Integer> allergiesIds;

    private BigDecimal price;

    private String type;

    /**
     * Instantiates a new Pizza. Also adds all the allergens of toppings if they haven't been added already.
     *
     * @param toppings toppings on the pizza
     * @param allergiesIds all the allergens pizza contains
     * @param price price of the pizza
     * @param type type or name of the pizza
     */
    public Pizza(List<Topping> toppings, Set<Integer> allergiesIds, BigDecimal price, String type) {
        this.toppings = toppings;
        this.allergiesIds = allergiesIds;
        this.price = price;
        this.type = type;
        for (Topping t : toppings) {
            for (Integer allergyId : t.getAllergiesIds()) {
                allergiesIds.add(allergyId);
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
            for (Integer allergyId : t.getAllergiesIds()) {
                allergiesIds.add(allergyId);
            }
        }
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public Set<Integer> getAllergiesIds() {
        return allergiesIds;
    }

    public void setAllergiesIds(Set<Integer> allergiesIds) {
        this.allergiesIds = allergiesIds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
