package nl.tudelft.sem.template.order.domain;

import java.io.Serializable;
import java.util.Set;

public class Topping implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    private transient String toppingName;

    private transient Set<Integer> allergiesIds;

    /**
     * Instantiates a new Topping.
     *
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
