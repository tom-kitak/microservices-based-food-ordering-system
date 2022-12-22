package nl.tudelft.sem.group06b.store.integration;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.group06b.store.api.StoreController;
import nl.tudelft.sem.group06b.store.authentication.AuthManager;
import nl.tudelft.sem.group06b.store.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import nl.tudelft.sem.group06b.store.domain.store.StoreAlreadyExistException;
import nl.tudelft.sem.group06b.store.model.ModifyStoreRequestModel;
import nl.tudelft.sem.group06b.store.service.StoreService;
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
public class StoreControllerTest {
    @MockBean
    private StoreService mockStoreService;

    @MockBean
    private AuthManager mockAuthManager;

    @MockBean
    private JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreController storeController;

    @Test
    public void testQueryAllStoresAsManager() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        List<Store> stores = List.of(
                new Store("store1", new Location("l1")),
                new Store("store2", new Location("l2"))
        );
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockStoreService.queryAllStores()).thenReturn(stores);
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(get("/api/stores/showAllStores")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        List<Store> resultStores = mapper.readValue(result.andReturn().getResponse().getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Store.class));
        assert resultStores.equals(stores);
    }

    @Test
    public void testQueryAllStoresAsRegular() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(get("/api/stores/showAllStores")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testAddStoreCorrect() throws Exception {
        ModifyStoreRequestModel request = new ModifyStoreRequestModel("store1", "l1", "manager");
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(post("/api/stores/addStore")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request)));

        result.andExpect(status().isOk());
        verify(mockStoreService).addStore(request.getName(), new Location(request.getStoreLocation()), request.getManager());
    }

    @Test
    public void testAddStoreException() throws Exception {
        ModifyStoreRequestModel request = new ModifyStoreRequestModel("store1", "l1", "manager");
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doThrow(new StoreAlreadyExistException(new Location("l1"))).when(mockStoreService).addStore(request.getName(), new Location(request.getStoreLocation()), request.getManager());
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(post("/api/stores/addStore")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request)));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Store already exists at l1"));
        verify(mockStoreService).addStore(request.getName(), new Location(request.getStoreLocation()), request.getManager());
        // Verify the error message
    }

    @Test
    public void testAddStoreAsCustomer() throws Exception {
        ModifyStoreRequestModel request = new ModifyStoreRequestModel("store1", "l1", "manager");
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(post("/api/stores/addStore")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request)));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Only regional managers can add stores"));
        verifyNoMoreInteractions(mockStoreService);
    }

    @Test
    public void testRemoveStoreCorrect() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(delete("/api/stores/removeStore/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("Store removed!");
        verify(mockStoreService).removeStore(2L);

    }

    @Test
    public void testRemoveStoreException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        doThrow(new NoSuchStoreException()).when(mockStoreService).removeStore(2L);

        ResultActions result = mockMvc.perform(delete("/api/stores/removeStore/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Store does not exist"));
        verify(mockStoreService).removeStore(2L);
    }

    @Test
    public void testRemoveStoreAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(delete("/api/stores/removeStore/2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Only regional managers can remove stores"));
        verifyNoMoreInteractions(mockStoreService);
    }

    @Test
    public void testValidateLocation() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockStoreService.validateStoreLocation(new Location("l1"))).thenReturn(true);

        ResultActions result = mockMvc.perform(get("/api/stores/validateLocation/l1")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("true");

    }

    @Test
    public void testValidateManager() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");
        when(mockStoreService.validateManager("manager")).thenReturn(false);

        ResultActions result = mockMvc.perform(get("/api/stores/validateManager/manger")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("false");
    }

    @Test
    public void testGetStoreIdAsRegional() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockStoreService.retrieveStoreId(new Location("l2"))).thenReturn(1L);

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreId/l2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("1");
    }

    @Test
    public void testGetStoreIdAsStore() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("store_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("store_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockStoreService.validateManager("user")).thenReturn(true);
        when(mockStoreService.retrieveStoreId(new Location("l2"))).thenReturn(1L);

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreId/l2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("1");
    }

    @Test
    public void testGetStoreIdException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new NoSuchStoreException()).when(mockStoreService).retrieveStoreId(new Location("l2"));

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreId/l2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Store does not exist"));
    }

    @Test
    public void testGetStoreIdAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreId/l2")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Only store managers and regional managers can get store Ids"));
    }

    @Test
    public void testGetStoreIdFromManagerAsRegional() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockStoreService.retrieveStoreId("stan")).thenReturn(1L);

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreIdManager/stan")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("1");
    }

    @Test
    public void testGetStoreIdFromManagerAsStore() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("store_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("store_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockStoreService.validateManager("user")).thenReturn(true);
        when(mockStoreService.retrieveStoreId("stan")).thenReturn(1L);

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreIdManager/stan")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isOk());
        assert result.andReturn().getResponse().getContentAsString().equals("1");
    }

    @Test
    public void testGetStoreIdFromManagerException() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("regional_manager");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new NoSuchStoreException()).when(mockStoreService).retrieveStoreId("stan");

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreIdManager/stan")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Store does not exist"));
    }

    @Test
    public void testGetStoreIdFromManagerAsCustomer() throws Exception {
        when(mockAuthManager.getRole()).thenReturn("customer");
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken("token")).thenReturn("user");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        ResultActions result = mockMvc.perform(get("/api/stores/getStoreIdManager/stan")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isBadRequest()).andExpect(status().reason("Only store managers and regional managers can get store Ids"));
    }
}

