package nl.tudelft.sem.template.order.domain;

import java.util.*;

public class Topping {

    private String toppingName;
    private Set<Integer> allergiesIds;

    /**
     * Instantiates a new Topping.
     * @param toppingName name of the topping
     * @param allergiesIds allergies of the topping
     */
    public Topping(String toppingName, Set<Integer> allergiesIds) {
        this.toppingName = toppingName;
        this.allergiesIds = allergiesIds;
    }

    public String getToppingName() {
        return toppingName;
    }

    public Set<Integer> getAllergiesIds() {
        return allergiesIds;
    }
}
