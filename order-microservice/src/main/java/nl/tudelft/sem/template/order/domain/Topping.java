package nl.tudelft.sem.template.order.domain;

import java.util.List;

public class Topping {

    private String toppingName;
    private Set<Allergy> allergies;

    /**
     * Instantiates a new Topping.
     * @param toppingName name of the topping
     * @param allergies allergies of the topping
     */
    public Topping(String toppingName, Set<Allergy> allergies) {
        this.toppingName = toppingName;
        this.allergies = allergies;
    }

    public String getToppingName() {
        return toppingName;
    }

    public Set<Allergy> getAllergies() {
        return allergies;
    }
}
