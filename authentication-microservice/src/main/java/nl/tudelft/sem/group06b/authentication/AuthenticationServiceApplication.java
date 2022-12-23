package nl.tudelft.sem.group06b.authentication;

import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.repository.RoleRepository;
import nl.tudelft.sem.group06b.authentication.repository.UserRepository;
import nl.tudelft.sem.group06b.authentication.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;

@SpringBootApplication()
@EntityScan("nl.tudelft.sem.group06b.authentication.domain")
@EnableJpaRepositories("nl.tudelft.sem.group06b.authentication.repository")
public class AuthenticationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Profile("!test")
    @Bean
    CommandLineRunner initializeDatabase(AuthenticationService authenticationService) throws Exception {
        return args -> {
            authenticationService.createRole(new RoleName("customer"));
            authenticationService.createRole(new RoleName("regional_manager"));

            authenticationService.register(new MemberId("group06b"), new Password("password"));
            authenticationService.changeRole(new MemberId("group06b"), new RoleName("regional_manager"));
        };
    }
}
