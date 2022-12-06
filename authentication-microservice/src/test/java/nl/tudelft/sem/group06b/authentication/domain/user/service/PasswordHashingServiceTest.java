package nl.tudelft.sem.group06b.authentication.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PasswordHashingServiceTest {

    @Autowired
    PasswordHashingService passwordHashingService;

    @Test
    void hashEqualityTest() {
        Password password1 = new Password("password1234");
        Password password2 = new Password("password1234");
        assertEquals(passwordHashingService.hash(password1), passwordHashingService.hash(password2));
    }
}