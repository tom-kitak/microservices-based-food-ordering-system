package nl.tudelft.sem.group06b.order.model.processor;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Order;

@Data
@AllArgsConstructor
public class FetchOrdersResponseModel {
    private Collection<Order> orders;
}
