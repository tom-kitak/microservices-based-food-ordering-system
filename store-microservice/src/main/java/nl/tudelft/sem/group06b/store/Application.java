<<<<<<<< HEAD:store-microservice/src/main/java/nl/tudelft/sem/group06b/store/Application.java
package nl.tudelft.sem.group06b.store;
========
package nl.tudelft.sem.group06b.user;
>>>>>>>> dev:user-microservice/src/main/java/nl/tudelft/sem/group06b/user/Application.java

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
