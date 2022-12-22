package nl.tudelft.sem.group06b.order.domain;

public class Allergen {

    private String allergenContent;

    public Allergen(String allergenContent) {
        this.allergenContent = allergenContent;
    }

    public String getAllergenContent() {
        return allergenContent;
    }

    public void setAllergenContent(String allergenContent) {
        this.allergenContent = allergenContent;
    }
}
