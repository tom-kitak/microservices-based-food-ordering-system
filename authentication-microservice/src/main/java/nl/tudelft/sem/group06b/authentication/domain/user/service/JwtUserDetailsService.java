package nl.tudelft.sem.group06b.authentication.domain.user.service;

import java.util.ArrayList;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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
     * @param memberIDValue The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    @Override
    public UserDetails loadUserByUsername(String memberIDValue) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByMemberID(new MemberID(memberIDValue));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();

        return new User(user.getMemberID().getMemberIDValue(), user.getPassword().toString(), new ArrayList<>());
    }
}

