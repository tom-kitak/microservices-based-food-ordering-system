package nl.tudelft.sem.group06b.authentication.domain.user.service;

import java.util.ArrayList;
import java.util.Collection;

import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.repository.RoleRepository;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final transient RoleRepository roleRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Loads user information required for authentication from the DB.
     *
     * @param memberIdValue The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    @Override
    public UserDetails loadUserByUsername(String memberIdValue) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByMemberId(new MemberId(memberIdValue));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(
                roleRepository.findById(user.getRoleId()).orElseThrow().getName().getRoleNameValue()));

        return new User(user.getMemberId().getMemberIdValue(), user.getPassword().toString(), new ArrayList<>());
    }
}

