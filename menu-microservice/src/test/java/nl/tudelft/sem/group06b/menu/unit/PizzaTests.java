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
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        this.pizza1 = new Pizza(41L, toppings, "Pepperoni", new BigDecimal("48.99"));
    }

    /**
     * tests if a pizza contains an allergen.
     */
    @Test
    public void containsAllergenTest() {
        Assertions.assertThat(pizza1.containsAllergen(new Allergy(42L, "Gluten")).isPresent()).isTrue();
        Assertions.assertThat(pizza1.containsAllergen(new Allergy(42L, "Gluten")).get()).isEqualTo("Gluten, Pepperoni, 41");
        Assertions.assertThat(pizza1.containsAllergen(new Allergy(52L, "Gluten")).isPresent()).isFalse();
    }

    @Test
    public void equalsTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepperoni", new BigDecimal("48.99"));
        Assertions.assertThat(this.pizza1.equals(pizza2)).isTrue();
        //Assertions.assertThat(this.pizza1.hashCode()).isEqualTo(pizza2.hashCode());
    }

    @Test
    public void notEqualIdTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(45L, toppings, "Pepperoni", new BigDecimal("48.99"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isFalse();
        //Assertions.assertThat(this.pizza1.hashCode()).isNotEqualTo(pizza2.hashCode());
    }

    @Test
    public void notEqualAllergiesTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(46L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepperoni", new BigDecimal("48.99"));
        Assertions.assertThat(this.pizza1.equals(pizza2)).isFalse();
        //Assertions.assertThat(this.pizza1.hashCode()).isNotEqualTo(pizza2.hashCode());
    }

    @Test
    public void notPizzaTest() {
        Assertions.assertThat(this.pizza1).isNotEqualTo(4);
    }

    @Test
    public void sameIds() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isTrue();
    }

    @Test
    public void notSameIds() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(43L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isFalse();
    }

    @Test
    public void notSameAllergyIds() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(44L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isFalse();
    }

    @Test
    public void notSameToppingIds() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L,  "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(4L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isFalse();
    }

    @Test
    public void notEqualHashCodes() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(43L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hashCode()).isNotEqualTo(pizza2.hashCode());
    }

    @Test
    public void sameIdsButDifferentToppings() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        allergies.add(new Allergy(54L, "Pizza"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        toppings.add(new Topping(4L, "Sardines", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isFalse();
    }

    @Test
    public void sameIdsButDifferentToppingSizes() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(42L, "Gluten"));
        allergies.add(new Allergy(53L, "Mushroom"));
        allergies.add(new Allergy(54L, "Pizza"));
        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        toppings.add(new Topping(2L, "Pepperoni", allergies, new BigDecimal("24.49")));
        Pizza pizza2 = new Pizza(41L, toppings, "Pepper", new BigDecimal("43.98"));
        Assertions.assertThat(this.pizza1.hasSameIds(pizza2)).isFalse();
    }

}
