package nl.tudelft.sem.group06b.authentication.service;

public interface AuthenticationService {

    /**
     * Authenticates a user with his name and password.
     *
     * @param username provided by the user
     * @param password provided by the user
     * @return the JWT token generated for authenticating the user
     */
    String authenticate(String username, String password);

    /**
     * Registers a user with this name and password.
     *
     * @param username provided by the new user
     * @param password provided by the new user
     */
    void register(String username, String password);
}
