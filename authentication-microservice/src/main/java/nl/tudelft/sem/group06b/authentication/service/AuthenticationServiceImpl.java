package nl.tudelft.sem.group06b.authentication.service;

import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.role.service.RoleCreationService;
import nl.tudelft.sem.group06b.authentication.domain.role.service.RoleCreationServiceImpl;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.service.JwtTokenGenerator;
import nl.tudelft.sem.group06b.authentication.domain.user.service.JwtTokenGeneratorImpl;
import nl.tudelft.sem.group06b.authentication.domain.user.service.JwtUserDetailsService;
import nl.tudelft.sem.group06b.authentication.domain.user.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient RegistrationService registrationService;

    private final transient RoleCreationService roleCreationService;

    /**
     * Constructor for the authentication controller.
     *
     * @param authenticationManager the manager for the authentication
     * @param jwtUserDetailsService the user details reader for jwt generation
     * @param registrationService the service that registers new users
     */
    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtUserDetailsService jwtUserDetailsService,
                                     JwtTokenGenerator jwtTokenGenerator,
                                     RegistrationService registrationService,
                                     RoleCreationServiceImpl roleCreationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.registrationService = registrationService;
        this.roleCreationService = roleCreationService;
    }

    @Override
    public String authenticate(MemberId memberId, Password password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberId, password));

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(memberId.getMemberIdValue());
        return jwtTokenGenerator.generateToken(userDetails);
    }

    @Override
    public void register(MemberId memberId, Password password) throws Exception {
        registrationService.registerUser(memberId, password);
    }

    @Override
    public void createRole(RoleName role) throws Exception {
        roleCreationService.addRole(role);
    }

    @Override
    public void changeRole(MemberId memberId, RoleName newRole) {
        registrationService.changeRole(memberId, newRole);
    }
}
 