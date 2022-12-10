package nl.tudelft.sem.group06b.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@ToString
@AllArgsConstructor
public class Pizza {
    public @Getter @Setter List<Topping> toppings;

}
