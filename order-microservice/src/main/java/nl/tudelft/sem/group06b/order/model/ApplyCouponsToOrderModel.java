package nl.tudelft.sem.group06b.order.model;

import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Pizza;

import java.util.List;

@Data
public class ApplyCouponsToOrderModel {
    List<Pizza> pizzas;
    List<String> couponsIds;
}
