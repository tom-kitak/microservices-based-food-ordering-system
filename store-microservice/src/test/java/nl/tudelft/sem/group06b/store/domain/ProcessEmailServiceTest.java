package nl.tudelft.sem.group06b.store.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import nl.tudelft.sem.group06b.store.database.EmailRepository;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.domain.email.ProcessEmailService;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProcessEmailServiceTest {
    @MockBean
    private EmailRepository mockEmailRepository;

    @MockBean
    private StoreRepository mockStoreRepository;

    @Autowired
    private ProcessEmailService processEmailService;

    @Test
    public void testReceiveEmail() throws Exception {
        Store s = new Store("test", new Location("location"), new ArrayList<>(), "manager");
        when(mockStoreRepository.findById(anyLong()))
                .thenReturn(Optional.of(s));

        processEmailService.receiveEmail("test", 1L);
        verify(mockEmailRepository).save(eq(new Email("test", s)));
        verify(mockEmailRepository).flush();
    }

    @Test
    public void testEmailToNonExistentStore() {
        when(mockStoreRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> processEmailService.receiveEmail("test", 1L));
    }

    @Test
    public void testDeleteEmail() throws Exception {
        Store s = new Store("test", new Location("location"), new ArrayList<>(), "manager");
        Email e = new Email("test", s);
        e.setId(1L);
        when(mockEmailRepository.findById(eq(1L)))
                .thenReturn(Optional.of(e));

        processEmailService.deleteEmail(1L);
        verify(mockEmailRepository).delete(eq(e));
        verify(mockEmailRepository).flush();
    }

    @Test
    public void testDeleteNonExistentEmail() {
        when(mockEmailRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> processEmailService.deleteEmail(1L));
    }
}
