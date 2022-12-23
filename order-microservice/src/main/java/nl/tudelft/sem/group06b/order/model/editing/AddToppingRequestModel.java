package nl.tudelft.sem.group06b.order.model.editing;

import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Pizza;

@Data
public class AddToppingRequestModel {
    private Long orderId;
    private Pizza pizza;
    private Long toppingId;
}
