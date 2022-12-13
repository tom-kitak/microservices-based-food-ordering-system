package nl.tudelft.sem.group06b.user.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.group06b.user.domain.Allergy;
import nl.tudelft.sem.group06b.user.domain.Location;
import nl.tudelft.sem.group06b.user.domain.User;
import nl.tudelft.sem.group06b.user.domain.UserRepository;
import nl.tudelft.sem.group06b.user.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserServiceTest {

    UserRepository repo;
    User user;
    UserService userService;

    @BeforeEach
    void setUp() {
        repo = mock(UserRepository.class);
        userService = new UserService(repo);
        user = new User("Kevin",
                List.of(new Allergy("Lactose"), new Allergy("Gluten")),
                new Location("Library"));
        when(repo.findByMemberId("Kevin")).thenReturn(java.util.Optional.of(user));
        when(repo.existsByMemberId("Kevin")).thenReturn(true);
    }

    @Test
    void addUserTest() throws Exception {
        User user1 = user = new User("Kevin12",
                List.of(new Allergy("Lactose"), new Allergy("Gluten")),
                new Location("Library"));
        assertEquals(userService.addUser("Kevin12",
                        List.of(new Allergy("Lactose"), new Allergy("Gluten")),
                        new Location("Library")), user1);
        assertThrows(Exception.class, () -> {
            userService.addUser("Kevin",
                List.of(new Allergy("Lactose"), new Allergy("Gluten")),
                new Location("Library"));
        });
    }

    @Test
    void getUserTest() throws Exception {
        assertEquals(userService.getUser("Kevin"),
                new User("Kevin",
                        List.of(new Allergy("Lactose"), new Allergy("Gluten")),
                        new Location("Library")));
    }

    @Test
    void addAllergyTest() throws Exception {
        assertEquals(userService.addAllergy("Kevin",
                        new Allergy("Collagen")).getAllergies(),
                List.of(new Allergy("Lactose"),
                        new Allergy("Gluten"),
                        new Allergy("Collagen")));
    }

    @Test
    void removeAllergyTest() throws Exception {
        assertEquals(userService.removeAllergy("Kevin", new Allergy("Gluten"))
                        .getAllergies(),
                List.of(new Allergy("Lactose")));
    }

    @Test
    void removeAllAllergiesTest() throws Exception {
        assertEquals(userService.removeAllAllergies("Kevin").getAllergies(),
                List.of());
    }

    @Test
    void updateLocationTest() throws Exception {
        assertEquals(userService.updateLocation("Kevin", new Location("Drebbelweg")).getPreferredLocation(),
                new Location("Drebbelweg"));
    }

    @Test
    void resetLocationTest() throws Exception {
        assertNull(userService.resetLocation("Kevin").getPreferredLocation());
    }

    @Test
    void checkMemberIdIsUniqueTest() {
        assertTrue(userService.checkMemberIdIsUnique("Kevin1"));
        assertFalse(userService.checkMemberIdIsUnique("Kevin"));
    }
}