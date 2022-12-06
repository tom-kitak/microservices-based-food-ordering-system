<<<<<<<< HEAD:example-microservice/src/main/java/nl/tudelft/sem/group06b/example/controllers/DefaultController.java
package nl.tudelft.sem.group06b.example.controllers;

import nl.tudelft.sem.group06b.example.authentication.AuthManager;
========
package nl.tudelft.sem.group06b.user.controllers;

import nl.tudelft.sem.group06b.user.authentication.AuthManager;
>>>>>>>> dev:example-microservice/src/main/java/nl/tudelft/sem/group06b/user/controllers/DefaultController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class DefaultController {

    private final transient AuthManager authManager;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public DefaultController(AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello " + authManager.getNetId());

    }

}
