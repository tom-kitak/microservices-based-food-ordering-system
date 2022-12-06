<<<<<<<< HEAD:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/Application.java
package nl.tudelft.sem.group06b.authentication;
========
package nl.tudelft.sem.group06b.user;
>>>>>>>> dev:example-microservice/src/main/java/nl/tudelft/sem/group06b/user/Application.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Example microservice application.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
