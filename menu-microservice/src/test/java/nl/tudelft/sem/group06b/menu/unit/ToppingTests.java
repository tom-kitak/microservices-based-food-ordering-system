package nl.tudelft.sem.group06b.menu.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToppingTests {
    private Topping topping1;

    /**
     * initializes toppings for tests.
     */
    @BeforeEach
    public void setup() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(43L, "Gluten"));
        this.topping1 = new Topping("Pepperoni", allergies, new BigDecimal("42.99"));
    }

    /**
     * checks if a topping contains allergens.
     */
    @Test
    public void containsAllergen() {
        Assertions.assertThat(topping1.containsAllergy(new Allergy(43L, "Gluten"))).isTrue();
        Assertions.assertThat(topping1.containsAllergy(new Allergy(44L, "Peanuts"))).isFalse();
    }
}
