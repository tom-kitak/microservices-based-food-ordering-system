package nl.tudelft.sem.group06b.user.domain;

import java.io.Serializable;


public class Allergy implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    /**
     * Class representing the allergy entity.
     */
    private transient String allergen;

    /**
     * Instantiates a new allergy.
     *
     * @param allergen the allergen component
     *
     */
    public Allergy(String allergen) {
        this.allergen = allergen;
    }

    public String getAllergen() {
        return allergen;
    }
}
