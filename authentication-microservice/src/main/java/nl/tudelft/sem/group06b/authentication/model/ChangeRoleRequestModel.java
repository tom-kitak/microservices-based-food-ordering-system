package nl.tudelft.sem.group06b.authentication.model;

import lombok.Data;

/**
 * Model representing an authentication response.
 */
@Data
public class ChangeRoleRequestModel {
    private String memberId;
    private String newRoleName;
}
