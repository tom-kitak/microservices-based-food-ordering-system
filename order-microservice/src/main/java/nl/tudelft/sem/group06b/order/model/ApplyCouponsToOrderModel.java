package nl.tudelft.sem.group06b.order.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.Pizza;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyCouponsToOrderModel {
    List<Pizza> pizzas;
    List<String> coupons;
}
