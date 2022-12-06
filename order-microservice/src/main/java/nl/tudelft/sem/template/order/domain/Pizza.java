package nl.tudelft.sem.template.order.domain;

import java.math.BigDecimal;
import java.util.*;

public class Pizza {

    private List<Topping> toppings;
    private Set<Allergy> allergies;
    private BigDecimal price;
    private String type;

    /**
     * Instantiates a new Pizza.
     * @param toppings toppings on the pizza
     * @param allergies all the allergens pizza contains
     * @param price price of the pizza
     * @param type type or name of the pizza
     */
    public Pizza(List<Topping> toppings, Set<Allergy> allergies, BigDecimal price, String type) {
        this.toppings = toppings;
        this.allergies = allergies;
        this.price = price;
        this.type = type;
        for (Topping topping : toppings) {
            for (Allergy allergy : topping.getAllergies()) {
                this.allergies.add(allergy);
            }
        }
    }

    /**
     * Adds list of toppings to the pizza and also adds the allergies.
     * @param toppings list of toppings to add to the pizza
     */
    public void addToppings(List<Topping> toppings) {
        this.toppings.addAll(toppings);
        for (Topping topping : toppings) {
            for (Allergy allergy : topping.getAllergies()) {
                this.allergies.add(allergy);
            }
        }
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public Set<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<Allergy> allergies) {
        this.allergies = allergies;
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
