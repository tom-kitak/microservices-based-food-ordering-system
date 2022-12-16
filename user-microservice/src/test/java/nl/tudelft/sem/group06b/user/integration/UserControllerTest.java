package nl.tudelft.sem.group06b.user.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.group06b.user.authentication.AuthManager;
import nl.tudelft.sem.group06b.user.authentication.JwtTokenVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;


    @Test
    public void registerUserTest() throws Exception {
        when(mockAuthenticationManager.getMemberId()).thenReturn("kevin12");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("kevin12");

        ResultActions result = mockMvc.perform(post("/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    public void getAllergensTest() throws Exception {
        when(mockAuthenticationManager.getMemberId()).thenReturn("kevin12");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("kevin12");

        //Firstly register the user
        mockMvc.perform(post("/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        mockMvc.perform(put("/user/kevin12/lactose/addAllergen")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        mockMvc.perform(put("/user/kevin12/fruit/addAllergen")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        ResultActions result = mockMvc.perform(get("/user/kevin12/getAllergens")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        Assertions.assertEquals(response, "[{\"allergen\":\"lactose\"},{\"allergen\":\"fruit\"}]");

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    public void addAllergenTest() throws Exception {
        when(mockAuthenticationManager.getMemberId()).thenReturn("kevin1");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("kevin1");

        //Firstly register the user
        mockMvc.perform(post("/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        ResultActions result = mockMvc.perform(put("/user/kevin1/lactose/addAllergen")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        Assertions.assertEquals(response, "[{\"allergen\":\"lactose\"}]");

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    public void removeAllergenTest() throws Exception {
        when(mockAuthenticationManager.getMemberId()).thenReturn("kevin123");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("kevin123");

        //Firstly register the user
        mockMvc.perform(post("/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        mockMvc.perform(put("/user/kevin123/lactose/addAllergen")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        ResultActions result = mockMvc.perform(delete("/user/kevin123/lactose/removeAllergen")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        Assertions.assertEquals(response, "[]");

        // Assert
        result.andExpect(status().isOk());

    }

    @Test
    public void updateLocationTest() throws Exception {
        when(mockAuthenticationManager.getMemberId()).thenReturn("kevin124");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("kevin124");

        //Firstly register the user
        mockMvc.perform(post("/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        ResultActions result = mockMvc.perform(put("/user/kevin124/drebbelweg/updateLocation")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);

        Assertions.assertEquals(response, "{\"address\":\"drebbelweg\"}");

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    public void resetLocationTest() throws Exception {
        when(mockAuthenticationManager.getMemberId()).thenReturn("kevin125");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("kevin125");

        //Firstly register the user
        mockMvc.perform(post("/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        ResultActions result = mockMvc.perform(delete("/user/kevin125/resetLocation")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isOk());
    }
}
