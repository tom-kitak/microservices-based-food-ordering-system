package nl.tudelft.sem.group06b.store.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.group06b.store.database.EmailRepository;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.domain.email.QueryEmailService;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class QueryEmailServiceTest {
    @MockBean
    private EmailRepository mockEmailRepository;

    @MockBean
    private StoreRepository mockStoreRepository;

    @Autowired
    private QueryEmailService queryEmailService;

    @Test
    public void testQueryAllEmails() {
        Store s = new Store("death", new Location("help"));
        List<Email> emails = List.of(new Email("test", s), new Email("test2", s));
        when(mockEmailRepository.findAll()).thenReturn(emails);
        List<Email> result = queryEmailService.queryAllEmails();
        assert(result.size() == 2);
        assert result.equals(emails);
    }

    @Test
    public void testGetEmailsFromStore() throws Exception {
        Store s = new Store("death", new Location("help"));
        List<Email> emails = List.of(new Email("test", s), new Email("test2", s));
        when(mockEmailRepository.retrieveEmailsByStoreId(1L)).thenReturn(emails);
        when(mockStoreRepository.findById(1L)).thenReturn(java.util.Optional.of(s));

        List<Email> result = queryEmailService.getEmailsFromStore(1L);
        assert(result.size() == 2);
        assert result.equals(emails);
    }

    @Test
    public void testGetEmailsFromNonExistingStore() throws Exception {
        when(mockStoreRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchStoreException.class, () -> queryEmailService.getEmailsFromStore(1L));
    }

    @Test
    public void testGetManagerFromStore() throws Exception {
        Store s = new Store("death", new Location("help"), new ArrayList<>(), "a human");
        when(mockStoreRepository.findById(1L)).thenReturn(java.util.Optional.of(s));

        String result = queryEmailService.getManagerFromStore(1L);
        assert result.equals("a human");
    }

    @Test
    public void testGetManagerFromNonExistingStore() {
        when(mockStoreRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchStoreException.class, () -> queryEmailService.getManagerFromStore(1L));
    }
}
