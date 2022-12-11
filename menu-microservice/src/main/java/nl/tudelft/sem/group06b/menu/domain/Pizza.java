package nl.tudelft.sem.group06b.menu.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Pizza {
    public @Getter @Setter List<Topping> toppings;

}
