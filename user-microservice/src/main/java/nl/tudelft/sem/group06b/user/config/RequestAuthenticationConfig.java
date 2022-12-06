<<<<<<<< HEAD:example-microservice/src/main/java/nl/tudelft/sem/group06b/example/config/RequestAuthenticationConfig.java
package nl.tudelft.sem.group06b.example.config;

import nl.tudelft.sem.group06b.example.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.group06b.example.authentication.JwtRequestFilter;
========
package nl.tudelft.sem.group06b.user.config;

import nl.tudelft.sem.group06b.user.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.group06b.user.authentication.JwtRequestFilter;
>>>>>>>> dev:user-microservice/src/main/java/nl/tudelft/sem/group06b/user/config/RequestAuthenticationConfig.java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Web security config.
 */
@Configuration
public class RequestAuthenticationConfig extends WebSecurityConfigurerAdapter {
    private final transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final transient JwtRequestFilter jwtRequestFilter;

    public RequestAuthenticationConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                       JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
