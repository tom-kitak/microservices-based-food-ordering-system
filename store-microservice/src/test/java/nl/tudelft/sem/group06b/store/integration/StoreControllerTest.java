package nl.tudelft.sem.group06b.store.integration;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.group06b.store.api.StoreController;
import nl.tudelft.sem.group06b.store.authentication.AuthManager;
import nl.tudelft.sem.group06b.store.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.store.Store;
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

}
