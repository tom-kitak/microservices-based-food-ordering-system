<<<<<<<< HEAD:example-microservice/src/main/java/nl/tudelft/sem/group06b/example/authentication/AuthManager.java
package nl.tudelft.sem.group06b.example.authentication;
========
package nl.tudelft.sem.group06b.user.authentication;
>>>>>>>> dev:example-microservice/src/main/java/nl/tudelft/sem/group06b/user/authentication/AuthManager.java

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication Manager.
 */
@Component
public class AuthManager {
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The name of the user.
     */
    public String getNetId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
