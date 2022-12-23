package nl.tudelft.sem.group06b.store.domain;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.store.ValidateStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ValidateStoreServiceTest {
    @MockBean
    private StoreRepository mockStoreRepository;

    @Autowired
    private ValidateStoreService validateStoreService;

    @Test
    public void testValidateStoreLocation() {
        when(mockStoreRepository.existsByStoreLocation(eq(new Location("test")))).thenReturn(true);
        assert (validateStoreService.validateStoreLocation(new Location("test")));
    }

    @Test
    public void testValidateNonExistentLocation() {
        when(mockStoreRepository.existsByStoreLocation(eq(new Location("test")))).thenReturn(false);
        assert (!validateStoreService.validateStoreLocation(new Location("test")));
    }

    @Test
    public void testValidateManager() {
        when(mockStoreRepository.existsByManager(eq("test"))).thenReturn(true);
        assert (validateStoreService.validateManager("test"));
    }

    @Test
    public void testValidateNonExistentManager() {
        when(mockStoreRepository.existsByManager(eq("test"))).thenReturn(false);
        assert (!validateStoreService.validateManager("test"));
    }
}
