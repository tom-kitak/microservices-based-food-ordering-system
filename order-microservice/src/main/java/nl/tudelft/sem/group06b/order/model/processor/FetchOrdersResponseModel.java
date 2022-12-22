package nl.tudelft.sem.group06b.order.model.processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Order;

import java.util.Collection;

@Data
@AllArgsConstructor
public class FetchOrdersResponseModel {
    private Collection<Order> orders;
}
