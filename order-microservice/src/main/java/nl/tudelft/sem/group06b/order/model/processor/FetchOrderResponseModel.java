package nl.tudelft.sem.group06b.order.model.processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Order;

@Data
@AllArgsConstructor
public class FetchOrderResponseModel {
    Order order;
}
