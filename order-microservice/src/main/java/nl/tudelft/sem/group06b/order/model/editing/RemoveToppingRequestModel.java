package nl.tudelft.sem.group06b.order.model.editing;

import lombok.Data;

@Data
public class RemoveToppingRequestModel {
    private Long pizzaId;
    private Long toppingId;
}
