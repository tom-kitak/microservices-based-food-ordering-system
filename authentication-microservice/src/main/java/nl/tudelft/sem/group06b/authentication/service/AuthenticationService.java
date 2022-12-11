package nl.tudelft.sem.group06b.authentication.service;

import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;

public interface AuthenticationService {

    /**
     * Authenticates a user with his name and password.
     *
     * @param memberID provided by the user
     * @param password provided by the user
     * @return the JWT token generated for authenticating the user
     */
    String authenticate(MemberID memberID, Password password);

    /**
     * Registers a user with this name and password.
     *
     * @param memberID provided by the new user
     * @param password provided by the new user
     */
    void register(MemberID memberID, Password password) throws Exception;
}
