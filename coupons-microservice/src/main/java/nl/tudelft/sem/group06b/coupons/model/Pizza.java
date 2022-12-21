package nl.tudelft.sem.group06b.coupons.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {
    long pizzaId;
    List<Long> toppings;
    BigDecimal price;
}
