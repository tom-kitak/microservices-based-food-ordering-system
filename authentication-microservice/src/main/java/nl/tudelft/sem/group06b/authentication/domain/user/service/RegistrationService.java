package nl.tudelft.sem.group06b.authentication.domain.user.service;

import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.User;

public interface RegistrationService {

    /**
     * Register a new user.
     *
     * @param memberId The memberId of the user
     * @param password The password of the user
     * @throws Exception if the user already exists
     */
    User registerUser(MemberId memberId, Password password) throws Exception;

    /**
     * Changes the role of a user given their memberId.
     *
     * @param memberId the id of the user with a new role
     * @param roleName the new name of the role that is attributed to the user
     */
    void changeRole(MemberId memberId, RoleName roleName);
}
