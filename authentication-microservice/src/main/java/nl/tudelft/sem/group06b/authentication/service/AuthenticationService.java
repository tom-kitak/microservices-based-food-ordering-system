package nl.tudelft.sem.group06b.authentication.service;

import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;

public interface AuthenticationService {

    /**
     * Authenticates a user with his name and password.
     *
     * @param memberId provided by the user
     * @param password provided by the user
     * @return the JWT token generated for authenticating the user
     */
    String authenticate(MemberId memberId, Password password);

    /**
     * Registers a user with this name and password.
     *
     * @param memberId provided by the new user
     * @param password provided by the new user
     * @throws Exception in case something fails or if a user with the same memberID exists
     */
    void register(MemberId memberId, Password password) throws Exception;

    /**
     * Creates a new role.
     *
     * @param role the name of the new role that is wanted to be created
     * @throws Exception in case something fails or if a role with the same name already exists.
     */
    void createRole(RoleName role) throws Exception;

    /**
     * Changes the role of an existing user.
     *
     * @param memberId the unique identifier of the user that will be assigned a new role
     * @param newRole the new role of the user
     */
    void changeRole(MemberId memberId, RoleName newRole);
}
