package nl.tudelft.sem.group06b.order.domain;

import java.io.Serializable;
import java.util.Set;

public class Topping implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    private String toppingName;

    private Set<String> allergenIds;

    /**
     * Instantiates a new Topping.
     *
     * @param toppingName name of the topping
     * @param allergenIds allergies of the topping
     */
    public Topping(String toppingName, Set<String> allergenIds) {
        this.toppingName = toppingName;
        this.allergenIds = allergenIds;
    }

    public String getToppingName() {
        return toppingName;
    }

    public Set<String> getAllergenIds() {
        return allergenIds;
    }
}
