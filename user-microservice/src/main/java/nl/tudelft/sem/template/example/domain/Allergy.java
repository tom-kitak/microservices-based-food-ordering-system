package nl.tudelft.sem.template.example.domain;

import java.io.Serializable;


public class Allergy implements Serializable {
    /**
     * Class representing the allergy entity
     */
    private String allergen;

    /**
     * Instantiates a new allergy
     * @param allergen
     */
    public Allergy(String allergen){
        this.allergen = allergen;
    }

    public String getAllergen() {
        return allergen;
    }
}
