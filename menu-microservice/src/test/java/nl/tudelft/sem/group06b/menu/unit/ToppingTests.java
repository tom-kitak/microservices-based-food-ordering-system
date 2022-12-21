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
        this.topping1 = new Topping(2L, "Pepperoni", allergies, new BigDecimal("42.99"));
    }

    /**
     * checks if a topping contains allergens.
     */
    @Test
    public void containsAllergen() {
        Assertions.assertThat(topping1.containsAllergy(new Allergy(43L, "Gluten")).isPresent()).isTrue();
        Assertions.assertThat(topping1.containsAllergy(new Allergy(43L, "Gluten")).get()).isEqualTo("Gluten, Pepperoni");
        Assertions.assertThat(topping1.containsAllergy(new Allergy(44L, "Peanuts")).isPresent()).isFalse();
    }

    @Test
    public void equalsTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(43L, "Gluten"));
        Topping topping2 = new Topping(2L, "Pepperoni", allergies, new BigDecimal("42.99"));
        Assertions.assertThat(this.topping1.equals(topping2)).isTrue();
    }

    @Test
    public void notEqualIdTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(43L, "Gluten"));
        Topping topping2 = new Topping(3L, "Pepperoni", allergies, new BigDecimal("42.99"));
        Assertions.assertThat(this.topping1.equals(topping2)).isFalse();
    }

    @Test
    public void notEqualAllergiesTest() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(45L, "Gluten"));
        Topping topping2 = new Topping(2L, "Pepperoni", allergies, new BigDecimal("42.99"));
        Assertions.assertThat(this.topping1.equals(topping2)).isFalse();
    }

    @Test
    public void sameIdTests() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(43L, "Gluten"));
        Topping topping2 = new Topping(2L, "Pepper", allergies, new BigDecimal("44.99"));
        Assertions.assertThat(this.topping1.hasSameIds(topping2)).isTrue();
    }


    @Test
    public void notSameIdTests() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(43L, "Gluten"));
        Topping topping2 = new Topping(3L, "Pepper", allergies, new BigDecimal("44.99"));
        Assertions.assertThat(this.topping1.hasSameIds(topping2)).isFalse();
    }

    @Test
    public void notSameAllergyIdTests() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(46L, "Gluten"));
        Topping topping2 = new Topping(2L, "Pepper", allergies, new BigDecimal("44.99"));
        Assertions.assertThat(this.topping1.hasSameIds(topping2)).isFalse();
    }

    @Test
    public void notToppingTest() {
        Assertions.assertThat(this.topping1).isNotEqualTo(5);
    }

    @Test
    public void notEqualHashCode() {
        ArrayList<Allergy> allergies = new ArrayList<>();
        allergies.add(new Allergy(46L, "Gluten"));
        Topping topping2 = new Topping(2L, "Pepper", allergies, new BigDecimal("44.99"));
        Assertions.assertThat(this.topping1.hashCode()).isNotEqualTo(topping2.hashCode());
    }

}
