package nl.tudelft.sem.group06b.store.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.store.ProcessStoreService;
import nl.tudelft.sem.group06b.store.domain.store.QueryStoreService;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import nl.tudelft.sem.group06b.store.domain.store.ValidateStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class StoreServiceTest {
    @MockBean
    private ProcessStoreService mockProcessStoreService;

    @MockBean
    private QueryStoreService mockQueryStoreService;

    @MockBean
    private ValidateStoreService mockValidateStoreService;

    @Autowired
    private StoreService storeService;

    @Test
    public void testAddStore() throws Exception {
        storeService.addStore("name", new Location("hell"), "manager");
        verify(mockProcessStoreService, times(1))
                .addStore("name", new Location("hell"), "manager");
    }

    @Test
    public void testRemoveStore() throws Exception {
        storeService.removeStore(1L);
        verify(mockProcessStoreService, times(1)).removeStore(1L);
    }

    @Test
    public void testValidateStoreLocation() {
        when(mockValidateStoreService.validateStoreLocation(new Location("hell")))
                .thenReturn(true);
        assert storeService.validateStoreLocation(new Location("hell"));
        verify(mockValidateStoreService, times(1))
                .validateStoreLocation(new Location("hell"));
    }

    @Test
    public void testValidateManager() {
        when(mockValidateStoreService.validateManager("manager")).thenReturn(true);
        assert storeService.validateManager("manager");
        verify(mockValidateStoreService, times(1)).validateManager("manager");
    }

    @Test
    public void testQueryAllStores() {
        List<Store> stores = List.of(new Store("name", new Location("hell")));
        when(mockQueryStoreService.queryAllStores()).thenReturn(stores);
        assert storeService.queryAllStores().equals(stores);
        verify(mockQueryStoreService, times(1)).queryAllStores();
    }

    @Test
    public void testRetrieveStoreIdByLocation() throws Exception {
        when(mockQueryStoreService.retrieveStoreId(new Location("hell")))
                .thenReturn(1L);
        assert storeService.retrieveStoreId(new Location("hell")) == 1L;
        verify(mockQueryStoreService, times(1))
                .retrieveStoreId(new Location("hell"));
    }

    @Test
    public void testRetrieveStoreIdByManager() throws Exception {
        when(mockQueryStoreService.retrieveStoreId("manager")).thenReturn(1L);
        assert storeService.retrieveStoreId("manager") == 1L;
        verify(mockQueryStoreService, times(1)).retrieveStoreId("manager");
    }
}
