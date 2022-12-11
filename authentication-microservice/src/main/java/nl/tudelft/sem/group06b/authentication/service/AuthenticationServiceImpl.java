package nl.tudelft.sem.group06b.authentication.service;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.domain.user.service.JwtTokenGenerator;
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
                                    RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.registrationService = registrationService;
    }

    @Override
    public String authenticate(MemberID memberID, Password password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberID, password));

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(memberID.getMemberIDValue());
        final String jwtToken = jwtTokenGenerator.generateToken(userDetails);
        return jwtToken;
    }

    @Override
    public void register(MemberID memberID, Password password) throws Exception {
        registrationService.registerUser(memberID, password);
    }
}
 