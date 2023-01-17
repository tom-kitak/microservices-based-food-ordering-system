package nl.tudelft.sem.group06b.order.model.editing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.Pizza;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPizza {
    private Long orderId;
    private Pizza pizza;
}
