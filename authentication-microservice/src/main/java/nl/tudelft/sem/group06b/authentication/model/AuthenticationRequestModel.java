<<<<<<<< HEAD:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/models/AuthenticationRequestModel.java
package nl.tudelft.sem.group06b.authentication.models;
========
package nl.tudelft.sem.group06b.authentication.model;
>>>>>>>> dev:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/model/AuthenticationRequestModel.java

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class AuthenticationRequestModel {
    private String username;
    private String password;
}
