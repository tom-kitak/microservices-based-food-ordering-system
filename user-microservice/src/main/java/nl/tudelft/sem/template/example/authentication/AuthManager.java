package nl.tudelft.sem.template.example.authentication;

import nl.tudelft.sem.template.example.domain.Role;
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
    public String getMemberId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The role of the user.
     */
    public Role getRole() {
        return (Role) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0];
    }
}
