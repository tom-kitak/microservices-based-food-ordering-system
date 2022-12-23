package nl.tudelft.sem.group06b.authentication.domain.role.service;

import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.role.RoleAlreadyExistsException;
import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.repository.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for creating a new role.
 */
@Service
public class RoleCreationServiceImpl implements RoleCreationService {
    private final transient RoleRepository roleRepository;

    /**
     * Instantiates a new RoleService.
     *
     * @param roleRepository the role repository
     */
    public RoleCreationServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void addRole(RoleName roleName) throws Exception {
        if (!checkRoleNameIsUnique(roleName)) {
            throw new RoleAlreadyExistsException(roleName);
        }
        roleRepository.save(new Role(roleName));
    }

    private boolean checkRoleNameIsUnique(RoleName roleName) {
        return !roleRepository.existsByRoleName(roleName);
    }
}
