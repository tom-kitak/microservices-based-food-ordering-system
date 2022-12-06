<<<<<<<< HEAD:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/authentication/JwtUserDetailsService.java
package nl.tudelft.sem.group06b.authentication.authentication;

import java.util.ArrayList;
import nl.tudelft.sem.group06b.authentication.domain.user.NetId;
import nl.tudelft.sem.group06b.authentication.domain.user.UserRepository;
========
package nl.tudelft.sem.group06b.authentication.domain.user.service;

import java.util.ArrayList;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
>>>>>>>> dev:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/domain/user/service/JwtUserDetailsService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User details service responsible for retrieving the user from the DB.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final transient UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user information required for authentication from the DB.
     *
     * @param username The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByUsername(new Username(username));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();

        ArrayList list = new ArrayList<>();
        list.add(user.getRoleId());

        return new User(user.getUsername().toString(), user.getPassword().toString(), list);
    }
}

