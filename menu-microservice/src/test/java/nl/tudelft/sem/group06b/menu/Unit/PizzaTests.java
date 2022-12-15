package nl.tudelft.sem.group06b.menu.Unit;

import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PizzaTests {
    Pizza p;
    @BeforeEach
    public void setup() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(42L, "Pepperoni", allergies, new BigDecimal("24.49")));
        this.p = new Pizza(42L, toppings, "Pepperoni", new BigDecimal("48.99"));
    }
    @Test
    public void containsAllergenTest(){
        Assertions.assertThat(p.containsAllergen(new Allergy(42L, "Gluten"))).isTrue();
        Assertions.assertThat(p.containsAllergen(new Allergy(52L, "Gluten"))).isFalse();
    }
}
