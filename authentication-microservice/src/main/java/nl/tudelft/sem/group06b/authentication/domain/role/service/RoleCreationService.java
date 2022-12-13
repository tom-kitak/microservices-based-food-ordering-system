package nl.tudelft.sem.group06b.authentication.domain.role.service;

import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;

public interface RoleCreationService {

    /**
     * Created a new role.
     *
     * @param roleName The name of the newly added role.
     * @throws Exception if the user already exists
     */
    void addRole(RoleName roleName) throws Exception;
}
