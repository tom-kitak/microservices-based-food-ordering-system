package nl.tudelft.sem.group06b.store.domain;

import java.util.Objects;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.Test;

public class EmailTest {
    @Test
    public void testGetId() {
        Email email = new Email();
        assert email.getId() == null;
    }

    @Test
    public void testSetId() {
        Email email = new Email();
        assert email.getId() == null;
        email.setId(1L);
        assert email.getId() == 1L;
    }

    @Test
    public void testGetDummyEmail() {
        Email email = new Email("death", new Store("test", new Location("location")));
        assert email.getDummyEmail().equals("death");
    }

    @Test
    public void testGetStore() {
        Store store = new Store("test", new Location("location"));
        Email email = new Email("death", store);
        assert email.getStore().equals(store);
    }

    @Test
    public void testSetStore() {
        Store store = new Store("test", new Location("location"));
        Email email = new Email("death", store);
        assert email.getStore().equals(store);
        Store store2 = new Store("test2", new Location("location2"));
        email.setStore(store2);
        assert email.getStore().equals(store2);
    }

    @Test
    public void testEquals() {
        Store store = new Store("test", new Location("location"));
        Email email = new Email("death", store);
        Email email2 = new Email("death", store);

        assert email.equals(email2);
        assert !email.equals(null);
        assert !email.equals(new Object());
        assert email.equals(email);

        email2.setId(1L);
        email.setId(2L);
        assert !email.equals(email2);

        email.setId(null);
        assert !email.equals(new Email("notDeath", store));
        assert !email.equals(
                new Email("death", new Store("notTest", new Location("notLocation")))
        );
    }

    @Test
    public void testHashCode() {
        Store store = new Store("test", new Location("location"));
        Email email = new Email("death", store);

        assert email.hashCode() == Objects.hash(null, "death", store);
    }
}
