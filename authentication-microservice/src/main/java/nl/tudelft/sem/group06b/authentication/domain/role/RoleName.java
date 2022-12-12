package nl.tudelft.sem.group06b.authentication.domain.role;

import java.util.Objects;

public class RoleName {
    private final transient String roleNameValue;

    public RoleName(String roleNameValue) {
        // validate roleName
        this.roleNameValue = roleNameValue;
    }

    public String getRoleNameValue() {
        return roleNameValue;
    }

    @Override
    public String toString() {
        return roleNameValue;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleName otherRoleName = (RoleName) o;
        return Objects.equals(roleNameValue, otherRoleName.roleNameValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleNameValue);
    }
}
