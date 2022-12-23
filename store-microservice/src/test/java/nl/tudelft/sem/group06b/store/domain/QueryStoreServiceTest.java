package nl.tudelft.sem.group06b.store.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchManagerException;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.QueryStoreService;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class QueryStoreServiceTest {
    @MockBean
    private StoreRepository mockStoreRepository;

    @Autowired
    private QueryStoreService queryStoreService;

    @Test
    public void testQueryAllStores() {
        List<Store> stores = List.of(
                new Store("test", new Location("l1")),
                new Store("test2", new Location("l2")),
                new Store("test3", new Location("l3"))
        );
        when(mockStoreRepository.findAll()).thenReturn(stores);

        List<Store> res = queryStoreService.queryAllStores();
        verify(mockStoreRepository).findAll();
        assert stores.equals(res);
    }

    @Test
    public void testRetrieveStoreId() throws Exception {
        Store s = new Store("test", new Location("l1"));
        s.setId(1L);
        when(mockStoreRepository.findByStoreLocation(new Location("l1"))).thenReturn(
                Optional.of(s));

        long res = queryStoreService.retrieveStoreId(new Location("l1"));
        verify(mockStoreRepository).findByStoreLocation(new Location("l1"));
        assert res == 1L;
    }

    @Test
    public void testRetrieveNonExistingStoreId() {
        when(mockStoreRepository.findByStoreLocation(new Location("l1"))).thenReturn(
                Optional.empty());

        assertThrows(NoSuchStoreException.class, () -> {
            queryStoreService.retrieveStoreId(new Location("l1"));
        });
    }

    @Test
    public void testRetrieveStoreIdByLocation() throws Exception {
        Store s = new Store("test", new Location("l1"), new ArrayList<>(), "manager");
        s.setId(1L);
        when(mockStoreRepository.findByManager("manager")).thenReturn(
                Optional.of(s));

        long res = queryStoreService.retrieveStoreId("manager");
        verify(mockStoreRepository).findByManager("manager");
        assert res == 1L;
    }

    @Test
    public void testRetrieveNonExistingStoreIdByLocation() {
        when(mockStoreRepository.findByManager("manager")).thenReturn(
                Optional.empty());

        assertThrows(NoSuchManagerException.class, () -> {
            queryStoreService.retrieveStoreId("manager");
        });
    }
}
