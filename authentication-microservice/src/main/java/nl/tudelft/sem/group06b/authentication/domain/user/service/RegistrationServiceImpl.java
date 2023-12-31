
package nl.tudelft.sem.group06b.authentication.domain.user.service;


import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberIdAlreadyInUseException;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.repository.RoleRepository;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */
@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final transient UserRepository userRepository;
    private final transient RoleRepository roleRepository;
    private final transient PasswordHashingService passwordHashingService;

    @Override
    public void registerUser(MemberId memberId, Password password) throws Exception {

        if (checkMemberIdIsUnique(memberId)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            Role role = roleRepository.findByRoleName(new RoleName("customer")).orElseThrow();
            User user = new User(memberId, hashedPassword, role);
            userRepository.save(user);

            return;
        }

        throw new MemberIdAlreadyInUseException(memberId);
    }

    @Override
    public void changeRole(MemberId memberId, RoleName roleName) {
        var optionalUser = userRepository.findByMemberId(memberId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();
        Role role = roleRepository.findByRoleName(roleName).orElseThrow();
        user.changeRole(role);
        userRepository.save(user);
    }

    private boolean checkMemberIdIsUnique(MemberId memberId) {
        return !userRepository.existsByMemberId(memberId);
    }
}
