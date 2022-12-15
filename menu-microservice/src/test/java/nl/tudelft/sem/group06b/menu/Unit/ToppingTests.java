package nl.tudelft.sem.group06b.menu.Unit;

import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ToppingTests {
    private Topping t;

    @BeforeEach
    public void setup() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(43L, "Gluten"));
        this.t = new Topping(42L, "Pepperoni", allergies, new BigDecimal("42.99"));
    }

    @Test
    public void containsAllergen() {
        Assertions.assertThat(t.containsAllergy(new Allergy(43L, "Gluten"))).isTrue();
        Assertions.assertThat(t.containsAllergy(new Allergy(44L, "Peanuts"))).isFalse();
    }
}
