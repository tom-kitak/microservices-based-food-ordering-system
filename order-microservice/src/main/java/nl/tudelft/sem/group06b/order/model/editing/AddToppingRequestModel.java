package nl.tudelft.sem.group06b.order.model.editing;

import lombok.Data;

@Data
public class AddToppingRequestModel {
    private Long pizzaId;
    private Long toppingId;
}
