package nl.tudelft.sem.group06b.menu.unit;

import nl.tudelft.sem.group06b.menu.domain.Allergy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AllergyTests {
    @Test
    public void equalsTest() {
        Allergy a = new Allergy(5L, "Gluten");
        Allergy b = new Allergy(5L, "Gluten");
        Allergy c = new Allergy(3L, "Gluten");
        Assertions.assertThat(a).isEqualTo(b);
        Assertions.assertThat(a).isNotEqualTo(c);
        Assertions.assertThat(a).isEqualTo(a);
    }
}
