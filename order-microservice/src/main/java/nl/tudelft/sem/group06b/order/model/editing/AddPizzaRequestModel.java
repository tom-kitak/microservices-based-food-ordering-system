package nl.tudelft.sem.group06b.order.model.editing;

import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Pizza;

@Data
public class AddPizzaRequestModel {
    Long orderId;
    Pizza pizza;
}
