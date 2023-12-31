
package nl.tudelft.sem.group06b.authentication.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
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
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegistrationServiceTest {

    @Autowired
    private transient RegistrationService registrationService;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private transient RoleRepository roleRepository;

    @Test
    public void createUser_withValidData_worksCorrectly() throws Exception {
        // Arrange
        final MemberId testUser = new MemberId("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);
        roleRepository.save(new Role(new RoleName("customer")));

        // Act
        registrationService.registerUser(testUser, testPassword);

        // Assert
        User savedUser = userRepository.findByMemberId(testUser).orElseThrow();

        assertThat(savedUser.getMemberId()).isNotEqualTo(null);
        assertThat(savedUser.getMemberId()).isNotEqualTo(savedUser);
        assertThat(savedUser.getMemberId()).isEqualTo(savedUser.getMemberId());
        assertThat(savedUser.getMemberId()).isEqualTo(testUser);
        assertThat(savedUser.getMemberId().hashCode()).isEqualTo(testUser.hashCode());
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
    }

    @Test
    public void createUser_withExistingUser_throwsException() throws Exception {
        // Arrange
        final MemberId testUser = new MemberId("SomeUser");
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Password newTestPassword = new Password("password456");
        roleRepository.save(new Role(new RoleName("customer")));

        User existingAppUser = new User(testUser, existingTestPassword,
                roleRepository.findByRoleName(new RoleName("customer")).orElseThrow());
        userRepository.save(existingAppUser);

        // Act
        ThrowingCallable action = () -> registrationService.registerUser(testUser, newTestPassword);

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);

        User savedUser = userRepository.findByMemberId(testUser).orElseThrow();

        assertThat(savedUser.getMemberId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(existingTestPassword);
        assertThat(savedUser.getRole())
                .isEqualTo(roleRepository.findByRoleName(new RoleName("customer")).orElseThrow());
    }

    @Test
    public void changeRole_withExistingUser_worksCorrectly() throws Exception {
        // Arrange
        final MemberId testUser = new MemberId("SomeUser");
        final HashedPassword testPassword = new HashedPassword("password123");
        final RoleName newRoleName = new RoleName("store_owner");
        roleRepository.save(new Role(new RoleName("customer")));
        roleRepository.save(new Role(newRoleName));

        User existingAppUser = new User(testUser, testPassword,
                roleRepository.findByRoleName(new RoleName("customer")).orElseThrow());
        userRepository.save(existingAppUser);

        // Act
        registrationService.changeRole(testUser, newRoleName);

        // Assert
        User savedUser = userRepository.findByMemberId(testUser).orElseThrow();

        assertThat(savedUser.getMemberId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testPassword);
        assertThat(savedUser.getRole().getId()).isEqualTo(roleRepository.findByRoleName(newRoleName).orElseThrow().getId());
    }

    @Test
    public void changeRole_withoutExistingUser_throwsException() {
        // Arrange
        final MemberId testUser = new MemberId("SomeUser");
        final HashedPassword testPassword = new HashedPassword("password123");
        final RoleName newRoleName = new RoleName("store_owner");
        roleRepository.save(new Role(new RoleName("customer")));

        // Act
        ThrowingCallable action = () -> registrationService.changeRole(testUser, newRoleName);

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);
    }
}
