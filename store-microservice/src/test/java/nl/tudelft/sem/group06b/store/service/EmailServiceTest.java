package nl.tudelft.sem.group06b.store.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.domain.email.ProcessEmailService;
import nl.tudelft.sem.group06b.store.domain.email.QueryEmailService;
import nl.tudelft.sem.group06b.store.domain.email.ValidateManagerService;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class EmailServiceTest {
    @MockBean
    private ProcessEmailService mockProcessEmailService;

    @MockBean
    private QueryEmailService mockQueryEmailService;

    @MockBean
    private ValidateManagerService mockValidateManagerService;

    @Autowired
    private EmailService emailService;

    @Test
    public void testQueryAllEmails() {
        Store s = new Store("store", new Location("hell"), new ArrayList<>(), "legit satan");
        List<Email> emails = List.of(new Email("email", s), new Email("test", s));
        when(mockQueryEmailService.queryAllEmails()).thenReturn(emails);
        assert (emailService.queryAllEmails().equals(emails));
        verify(mockQueryEmailService).queryAllEmails();
    }

    @Test
    public void testReceiveEmail() throws Exception {
        emailService.receiveEmail("email", 1L);
        verify(mockProcessEmailService).receiveEmail("email", 1L);
    }

    @Test
    public void testDeleteEmail() throws Exception {
        emailService.deleteEmail(1L);
        verify(mockProcessEmailService).deleteEmail(1L);
    }

    @Test
    public void testGetEmailsFromStore() throws Exception {
        Store s = new Store("store", new Location("hell"), new ArrayList<>(), "legit satan");
        List<Email> emails = List.of(new Email("email", s), new Email("test", s));
        when(mockQueryEmailService.getEmailsFromStore(1L)).thenReturn(emails);
        assert (emailService.getEmailsFromStore(1L).equals(emails));
        verify(mockQueryEmailService).getEmailsFromStore(1L);
    }

    @Test
    public void testValidateManager() throws Exception {
        when(mockValidateManagerService.validateManager("human")).thenReturn(true);
        assert emailService.validateManager("human");
        verify(mockValidateManagerService).validateManager("human");
    }

    @Test
    public void testGetManagerFromStore() throws Exception {
        when(mockQueryEmailService.getManagerFromStore(1L)).thenReturn("human");
        assert emailService.getManagerFromStore(1L).equals("human");
        verify(mockQueryEmailService).getManagerFromStore(1L);
    }
}
