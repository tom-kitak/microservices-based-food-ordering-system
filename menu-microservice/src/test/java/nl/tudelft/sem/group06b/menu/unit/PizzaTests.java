package nl.tudelft.sem.group06b.menu.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PizzaTests {
    Pizza pizza1;

    /**
     * Initializes a pizza to test.
     */
    @BeforeEach
    public void setup() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping("Pepperoni", allergies, new BigDecimal("24.49")));
        this.pizza1 = new Pizza(toppings, "Pepperoni", new BigDecimal("48.99"));
    }

    /**
     * tests if a pizza contains an allergen.
     */
    @Test
    public void containsAllergenTest() {
        Assertions.assertThat(pizza1.containsAllergen(new Allergy(42L, "Gluten"))).isTrue();
        Assertions.assertThat(pizza1.containsAllergen(new Allergy(52L, "Gluten"))).isFalse();
    }

    @Test
    public void equalsTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping("Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(toppings, "Pepperoni", new BigDecimal("48.99"));
        Assertions.assertThat(this.pizza1.equals(pizza2)).isTrue();
    }

    @Test
    public void equalsAllergies() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Peanuts"));
        allergies.add(new Allergy(53L, "Allergies"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping("Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(toppings, "Pepperoni", new BigDecimal("48.99"));
        Assertions.assertThat(this.pizza1.equals(pizza2)).isFalse();
    }

    @Test
    public void equalsName() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping("Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(toppings, "Hawaii", new BigDecimal("48.99"));
        Assertions.assertThat(this.pizza1.equals(pizza2)).isFalse();
    }

    @Test
    public void equalsPrice() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping("Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(toppings, "Pepperoni", new BigDecimal("48.98"));
        Assertions.assertThat(this.pizza1.equals(pizza2)).isFalse();
    }

}
