package nl.tudelft.sem.group06b.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import nl.tudelft.sem.group06b.user.domain.Allergy;
import nl.tudelft.sem.group06b.user.domain.Location;
import nl.tudelft.sem.group06b.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserTest {

    private User user;

    /**
     * Providing a template user for every test.
     */
    @BeforeEach
    public void setup() {
        user = new User("Kevin", List.of(new Allergy("Lactose"),
                new Allergy("Gluten")),
                new Location("Drebbelweg"));
    }

    @Test
    void getMemberId() {
        assertEquals(user.getMemberId(), "Kevin");
    }

    @Test
    void setMemberId() {
        user.setMemberId("K");
        assertEquals(user.getMemberId(), "K");
    }

    @Test
    void getAllergies() {
        assertEquals(user.getAllergies(), List.of(new Allergy("Lactose"), new Allergy("Gluten")));
    }

    @Test
    void setAllergies() {
        user.setAllergies(List.of(new Allergy("Honey")));
        assertEquals(user.getAllergies(), List.of(new Allergy("Honey")));
    }

    @Test
    void getPreferredLocation() {
        assertEquals(user.getPreferredLocation(), new Location("Drebbelweg"));
    }

    @Test
    void setPreferredLocation() {
        user.setPreferredLocation(new Location("EWI"));
        assertEquals(user.getPreferredLocation(), new Location("EWI"));
    }

    @Test
    void equalsUsers() {
        User user2 = user = new User("Kevin", List.of(new Allergy("Lactose"),
                new Allergy("Gluten")),
                new Location("Drebbelweg"));
        User user3 = new User("N7JIO", List.of(new Allergy("Sauce"),
                new Allergy("Gluten")),
                new Location("Library"));
        assertEquals(user, user2);
        assertNotEquals(user, user3);
    }
}
