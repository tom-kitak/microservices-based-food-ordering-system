package nl.tudelft.sem.group06b.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Allergens {

    private String allergensContent;

    public String getAllergens() {
        return allergensContent;
    }

    public void setAllergens(String allergensContent) {
        this.allergensContent = allergensContent;
    }
}
