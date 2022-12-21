package nl.tudelft.sem.group06b.coupons.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyCouponsRequestModel {
    List<Pizza> pizzas;
    List<String> coupons;
}
