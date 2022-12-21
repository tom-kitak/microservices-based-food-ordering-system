package nl.tudelft.sem.group06b.order.model.processor;

import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Order;

@Data
public class PlaceOrderResponseModel {
    Order order;
}
