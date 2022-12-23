package nl.tudelft.sem.group06b.order.domain;

public class AllergenResponse {

    private String allergenContent;
    private boolean containsResponse;

    public AllergenResponse(String allergenContent) {
        this.allergenContent = allergenContent;
        this.containsResponse = true;
    }

    public AllergenResponse() {
        this.containsResponse = false;
    }

    public String getAllergenContent() {
        return allergenContent;
    }

    /**
     * Sets the content of the AllergenContent.
     *
     * @param allergenContent content of the allergen message
     */
    public void setAllergenContent(String allergenContent) {
        this.allergenContent = allergenContent;
        if (allergenContent != null) {
            this.containsResponse = true;
        }
    }

    public boolean isContainsResponse() {
        return containsResponse;
    }

    public void setContainsResponse(boolean containsResponse) {
        this.containsResponse = containsResponse;
    }
}
