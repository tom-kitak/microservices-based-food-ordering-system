package nl.tudelft.sem.group06b.authentication.domain.user;

import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * A DDD value object representing a username in our domain.
 */
public class MemberID {
    private final transient String memberIDValue;

    public MemberID(String memberIDValue) {
        // validate username
        this.memberIDValue = memberIDValue;
    }

    public String getMemberIDValue() {
        return memberIDValue;
    }

    @Override
    public String toString() {
        return memberIDValue;
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
        MemberID otherMemberID = (MemberID) o;
        return Objects.equals(memberIDValue, otherMemberID.memberIDValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberIDValue);
    }
}
