<<<<<<<< HEAD:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/models/AuthenticationResponseModel.java
package nl.tudelft.sem.group06b.authentication.models;
========
package nl.tudelft.sem.group06b.authentication.model;
>>>>>>>> dev:authentication-microservice/src/main/java/nl/tudelft/sem/group06b/authentication/model/AuthenticationResponseModel.java

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing an authentication response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseModel {
    private String jwtToken;
}
