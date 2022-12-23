package nl.tudelft.sem.group06b.store.integration;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.group06b.store.api.EmailController;
import nl.tudelft.sem.group06b.store.authentication.AuthManager;
import nl.tudelft.sem.group06b.store.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.domain.email.NoSuchEmailException;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import nl.tudelft.sem.group06b.store.model.SendEmailRequestModel;
import nl.tudelft.sem.group06b.store.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {
    @MockBean
    private transient EmailService mockEmailService;

    @MockBean
    private transient AuthManager mockAuthManager;

    @MockBean
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient MockMvc mockMvc;

    @Autowired
    private transient EmailController emailController;

    @Test
    public void testQueryAllEmailsCorrectly() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        Store s = new Store("store", new Location("l1"));
        List<Email> emails = List.of(new Email("test", s), new Email("notTest", s));
        when(mockEmailService.queryAllEmails()).thenReturn(emails);

        ResultActions result = mockMvc.perform(get("/api/email/showAllEmails")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        String resultString = result.andReturn().getResponse().getContentAsString();
        List<Email> resultEmails = mapper.readValue(resultString, mapper.getTypeFactory()
                .constructCollectionType(List.class, Email.class));
        assert resultEmails.equals(emails);
    }

    @Test
    public void testQueryAllEmailsException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(get("/api/email/showAllEmails")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Only regional managers can view all emails"));
    }

    @Test
    public void testQueryEmailsByStoreCorrectly() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        Store s = new Store("store", new Location("l1"));
        s.setId(1L);
        List<Email> emails = List.of(new Email("test", s), new Email("notTest", s));

        when(mockEmailService.getEmailsFromStore(1L)).thenReturn(emails);

        ResultActions result = mockMvc.perform(get("/api/email/showEmailsByStoreId/1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        String resultString = result.andReturn().getResponse().getContentAsString();
        List<Email> resultEmails = mapper.readValue(resultString, mapper.getTypeFactory()
                .constructCollectionType(List.class, Email.class));
        assert resultEmails.equals(emails);
    }

    @Test
    public void testQueryEmailsByStoreException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new NoSuchStoreException()).when(mockEmailService).getEmailsFromStore(1L);

        ResultActions result = mockMvc.perform(get("/api/email/showEmailsByStoreId/1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Store does not exist"));
    }

    @Test
    public void testQueryEmailsByStoreAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(get("/api/email/showEmailsByStoreId/1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status()
                .reason("Only regional managers can view emails by store"));
    }

    @Test
    public void testSendEmailCorrectlyAsRegional() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        SendEmailRequestModel request = new SendEmailRequestModel(1L, "test email");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/email/sendEmail")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        assert result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("Email sent!");
        verify(mockEmailService).receiveEmail("test email", 1L);
    }

    @Test
    public void testSendEmailCorrectlyAsStore() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("store_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("store_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockEmailService.validateManager("user")).thenReturn(true);

        SendEmailRequestModel request = new SendEmailRequestModel(1L, "test email");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/email/sendEmail")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        assert result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("Email sent!");
        verify(mockEmailService).receiveEmail("test email", 1L);
    }

    @Test
    public void testSendEmailAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockAuthManager.getMemberId()).thenReturn("user");

        when(mockEmailService.validateManager("user")).thenReturn(false);

        SendEmailRequestModel request = new SendEmailRequestModel(1L, "test email");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/email/sendEmail")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isBadRequest()).andExpect(status()
                .reason("Only store managers and regional managers can send emails"));
        verify(mockEmailService, never()).receiveEmail("test email", 1L);
    }

    @Test
    public void testSendEmailException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new NoSuchStoreException()).when(mockEmailService).receiveEmail("test email", 1L);

        SendEmailRequestModel request = new SendEmailRequestModel(1L, "test email");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/email/sendEmail")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Store does not exist"));
    }

    @Test
    public void testDeleteEmailAsRegional() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmail/1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        verify(mockEmailService).deleteEmail(1L);
    }

    @Test
    public void testDeleteEmailAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockAuthManager.getMemberId()).thenReturn("user");

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmail/1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status()
                .reason("Only regional managers can delete emails"));
    }

    @Test
    public void testDeleteEmailException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new NoSuchEmailException()).when(mockEmailService).deleteEmail(1L);

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmail/1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("The email does not exist!"));
    }

    @Test
    public void testDeleteEmailByStoreAsRegional() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockEmailService.getManagerFromStore(2L)).thenReturn("notUser");

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmailByStore/1/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        verify(mockEmailService).deleteEmail(1L);
    }

    @Test
    public void testDeleteEmailByStoreAsManager() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("store_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("store_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockEmailService.validateManager("user")).thenReturn(true);

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmailByStore/1/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        verify(mockEmailService).deleteEmail(1L);
    }

    @Test
    public void testDeleteEmailsByStoreAsManagerOfStore() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("store_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("store_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockEmailService.validateManager("user")).thenReturn(false);
        when(mockEmailService.getManagerFromStore(2L)).thenReturn("user");

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmailByStore/1/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        verify(mockEmailService).deleteEmail(1L);
    }

    @Test
    public void testDeleteEmailsByStoreAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockAuthManager.getMemberId()).thenReturn("user");

        when(mockEmailService.validateManager("user")).thenReturn(false);
        when(mockEmailService.getManagerFromStore(2L)).thenReturn("notUser");

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmailByStore/1/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason(
                "Store manager can only delete emails from his own store,"
                        + " or you need to be a regional manager"));
    }

    @Test
    public void testDeleteEmailsByStoreException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockEmailService.getManagerFromStore(2L)).thenReturn("notUser");

        doThrow(new NoSuchEmailException()).when(mockEmailService).deleteEmail(1L);

        ResultActions result = mockMvc.perform(delete("/api/email/deleteEmailByStore/1/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("The email does not exist!"));
    }

}
