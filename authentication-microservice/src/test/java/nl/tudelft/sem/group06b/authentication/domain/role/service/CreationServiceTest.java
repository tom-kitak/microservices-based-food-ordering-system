package nl.tudelft.sem.group06b.authentication.domain.role.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import nl.tudelft.sem.group06b.authentication.domain.user.service.PasswordHashingService;
import nl.tudelft.sem.group06b.authentication.domain.user.service.RegistrationService;
import nl.tudelft.sem.group06b.authentication.repository.RoleRepository;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreationServiceTest {

    @Autowired
    private transient CreationService creationService;

    @Autowired
    private transient RoleRepository roleRepository;

    @Test
    public void createRole_withValidData_worksCorrectly() throws Exception {
        // Arrange
        final String roleName = "member";

        // Act
        creationService.addRole(roleName);

        // Assert
        Role savedRole = roleRepository.findByName(roleName).orElseThrow();

        assertThat(savedRole.getName()).isEqualTo(roleName);
    }

    @Test
    public void createRole_withExistingRole_throwsException() {
        // Arrange
        final String roleName = "member";
        final Role role = new Role(roleName);
        roleRepository.save(role);

        // Act
        ThrowingCallable action = () -> creationService.addRole(roleName);

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);

        Role savedRole = roleRepository.findByName(roleName).orElseThrow();

        assertThat(savedRole.getName()).isEqualTo(roleName);

        assertThat(roleRepository.count()).isEqualTo(1);
    }
}