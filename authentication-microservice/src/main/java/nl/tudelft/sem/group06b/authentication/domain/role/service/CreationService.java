package nl.tudelft.sem.group06b.authentication.domain.role.service;

import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.role.RoleAlreadyExistsException;
import nl.tudelft.sem.group06b.authentication.repository.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for creating a new role.
 */
@Service
public class CreationService {
    private final transient RoleRepository roleRepository;

    /**
     * Instantiates a new RoleService.
     *
     * @param roleRepository the role repository
     */
    public CreationService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Created a new role.
     *
     * @param roleName The name of the newly added role.
     * @throws Exception if the user already exists
     */
    public Role addRole(String roleName) throws Exception {

        if (checkRoleNameIsUnique(roleName)) {
            // Create new role
            Role role = new Role(roleName);
            roleRepository.save(role);

            return role;
        }

        throw new RoleAlreadyExistsException(roleName);
    }

    public boolean checkRoleNameIsUnique(String roleName) {
        return !roleRepository.existsByName(roleName);
    }
}
