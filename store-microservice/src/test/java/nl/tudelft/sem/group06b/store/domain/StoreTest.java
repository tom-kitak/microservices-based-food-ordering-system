package nl.tudelft.sem.group06b.store.domain;

import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.Test;

public class StoreTest {
    @Test
    public void getIdTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getId() == null;
    }

    @Test
    public void setIdTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getId() == null;
        store.setId(1L);
        assert store.getId() == 1L;
    }

    @Test
    public void getNameTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getName().equals("name");
    }

    @Test
    public void setNameTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getName().equals("name");
        store.setName("newName");
        assert store.getName().equals("newName");
    }

    @Test
    public void getStoreLocationTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getStoreLocation().equals(new Location("test"));
    }

    @Test
    public void setStoreLocationTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getStoreLocation().equals(new Location("test"));
        store.setStoreLocation(new Location("newTest"));
        assert store.getStoreLocation().equals(new Location("newTest"));
    }

    @Test
    public void getManagerTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getManager() == null;
    }

    @Test
    public void setManagerTest() {
        Store store = new Store("name", new Location("test"));
        assert store.getManager() == null;
        store.setManager("newManager");
        assert store.getManager().equals("newManager");
    }

    @Test
    public void equalsTest() {
        Store store = new Store("name", new Location("test"));
        assert store.equals(store);
        assert !store.equals(null);
        assert !store.equals(new Object());
        Store store2 = new Store("name", new Location("test"));
        store2.setId(1L);
        assert !store.equals(store2);
        assert !store.equals(new Store("name1", new Location("test")));
        assert !store.equals(new Store("name", new Location("test1")));
        assert store.equals(new Store("name", new Location("test")));
        assert !store.equals(new Store("name", new Location("test"), null, "manager"));
    }

    @Test
    public void hashTest() {
        Store store = new Store("name", new Location("test"));
        assert store.hashCode() == store.hashCode();
        assert store.hashCode() == new Store("name", new Location("test")).hashCode();
    }
}
