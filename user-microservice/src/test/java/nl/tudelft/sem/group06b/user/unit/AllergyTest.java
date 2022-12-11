package nl.tudelft.sem.group06b.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.sem.group06b.user.domain.Allergy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AllergyTest {

    private Allergy allergy;

    /**
     * Providing a template allergy for every test.
     */
    @BeforeEach
    public void setup() {
        allergy = new Allergy("Lactose");
    }

    @Test
    void getAllergen() {
        assertEquals(allergy.getAllergen(), "Lactose");
    }

    @Test
    void setAllergen() {
        allergy.setAllergen("Crackers");
        assertEquals(allergy.getAllergen(), "Crackers");
    }

    @Test
    void equalsAllergy() {
        Allergy allergy1 = new Allergy("Lactose");
        Allergy allergy2 = new Allergy("Gluten");
        assertEquals(allergy, allergy1);
        assertNotEquals(allergy, allergy2);
    }
}
