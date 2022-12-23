package nl.tudelft.sem.group06b.store.database;

import java.util.ArrayList;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void findByStoreLocation() {
        storeRepository.save(new Store("test", new Location("test"), new ArrayList<>(), "test"));
        assert storeRepository.findByStoreLocation(new Location("test")).isPresent();
    }

    @Test
    public void findByManager() {
        storeRepository.save(new Store("test", new Location("test"), new ArrayList<>(), "test"));
        assert storeRepository.findByManager("test").isPresent();
    }

    @AfterEach
    public void cleanUp() {
        storeRepository.deleteAll();
    }
}
