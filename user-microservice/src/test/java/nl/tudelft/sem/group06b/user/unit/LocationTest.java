package nl.tudelft.sem.group06b.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.sem.group06b.user.domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class LocationTest {

    private Location location;

    /**
     * Providing a template location for every test.
     */
    @BeforeEach
    public void setup() {
        location = new Location("Drebbelweg");
    }

    @Test
    void getLocation() {
        assertEquals(location.getAddress(), "Drebbelweg");
    }

    @Test
    void setLocation() {
        location.setAddress("EWI");
        assertEquals(location.getAddress(), "EWI");
    }

    @Test
    void equalsLocation() {
        Location location1 = new Location("Drebbelweg");
        Location location2 = new Location("EWI");
        assertEquals(location, location1);
        assertEquals(location, location);
        assertNotEquals(location, location2);
        assertNotEquals(location, null);
    }
}
