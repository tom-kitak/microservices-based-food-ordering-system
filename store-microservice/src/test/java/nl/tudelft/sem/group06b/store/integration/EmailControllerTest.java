package nl.tudelft.sem.group06b.store.integration;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.Store;
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

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Only regional managers can view emails by store"));
    }
}
