package nl.tudelft.sem.group06b.user.domain;

import java.io.Serializable;
import java.util.Objects;


public class Allergy implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    /**
     * Class representing the allergy entity.
     */
    private String allergen;

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

    public void setAllergen(String allergen) {
        this.allergen = allergen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Allergy allergy = (Allergy) o;
        return Objects.equals(allergen, allergy.allergen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allergen);
    }
}
