package nl.tudelft.sem.group06b.store.domain;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.email.ValidateManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ValidateManagerServiceTest {
    @MockBean
    private StoreRepository mockStoreRepository;

    @Autowired
    private ValidateManagerService validateManagerService;

    @Test
    public void testValidateManager() {
        when(mockStoreRepository.existsByManager(eq("test"))).thenReturn(true);
        assert (validateManagerService.validateManager("test"));
    }

    @Test
    public void testValidateNonExistentManager() {
        when(mockStoreRepository.existsByManager(eq("test"))).thenReturn(false);
        assert (!validateManagerService.validateManager("test"));
    }
}
