package nl.tudelft.sem.group06b.store.domain;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.ProcessStoreService;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import nl.tudelft.sem.group06b.store.domain.store.StoreAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProcessStoreServiceTest {
    @MockBean
    private transient StoreRepository mockStoreRepository;

    @Autowired
    private transient ProcessStoreService processStoreService;

    @Test
    public void checkLocationIsUniqueFalseTest() {
        Location loc = new Location("test");
        when(mockStoreRepository.existsByStoreLocation(eq(loc))).thenReturn(true);
        assert !processStoreService.checkLocationIsUnique(loc);
    }

    @Test
    public void checkLocationIsUniqueTrueTest() {
        Location loc = new Location("test");
        when(mockStoreRepository.existsByStoreLocation(eq(loc))).thenReturn(false);
        assert processStoreService.checkLocationIsUnique(loc);
    }

    @Test
    public void addStoreUniqueTest() throws Exception {
        Location loc = new Location("test");
        when(mockStoreRepository.existsByStoreLocation(eq(loc))).thenReturn(false);

        processStoreService.addStore("test", loc, "test");

        //verify(mockStoreRepository).flush();
        verify(mockStoreRepository).save(eq(new Store("test", loc, new ArrayList<>(), "test")));
        verify(mockStoreRepository).flush();
    }

    @Test
    public void addStoreNotUniqueTest() {
        Location loc = new Location("test");
        when(mockStoreRepository.existsByStoreLocation(eq(loc))).thenReturn(true);

        assertThrows(StoreAlreadyExistException.class, () -> {
            processStoreService.addStore("test", loc, "test");
        });
    }

    @Test
    public void removeStoreTest() throws Exception {
        Location loc = new Location("test");
        Store store = new Store("test", loc, new ArrayList<>(), "test");
        when(mockStoreRepository.findById(eq(1L))).thenReturn(java.util.Optional.of(store));

        processStoreService.removeStore(1L);

        verify(mockStoreRepository).delete(eq(store));
        verify(mockStoreRepository).flush();
    }

    @Test
    public void removeStoreExceptionTest() {
        when(mockStoreRepository.findById(eq(1L))).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchStoreException.class, () -> {
            processStoreService.removeStore(1L);
        });
    }
}
