package nl.tudelft.sem.group06b.store.domain;

import java.util.Objects;
import org.junit.jupiter.api.Test;

public class LocationTest {
    @Test
    public void getAddressTest() {
        Location location = new Location("test");
        assert location.getAddress().equals("test");
    }

    @Test
    public void setAddressTest() {
        Location location = new Location("test");
        assert location.getAddress().equals("test");
        location.setAddress("test2");
        assert location.getAddress().equals("test2");
    }

    @Test
    public void equalsTest() {
        Location location = new Location("test");
        Location location2 = new Location("test");
        assert location.equals(location2);
    }

    @Test
    public void equalsSameTest() {
        Location location = new Location("test");
        assert location.equals(location);
    }

    @Test
    public void equalsNullTest() {
        Location location = new Location("test");
        assert !location.equals(null);
    }

    @Test
    public void equalsDifferentClassTest() {
        Location location = new Location("test");
        assert !location.equals("test");
    }

    @Test
    public void equalsDifferentAddressTest() {
        Location location = new Location("test");
        Location location2 = new Location("test2");
        assert !location.equals(location2);
    }

    @Test
    public void hashCodeTest() {
        Location location = new Location("test");
        assert location.hashCode() == Objects.hash("test");
    }
}
