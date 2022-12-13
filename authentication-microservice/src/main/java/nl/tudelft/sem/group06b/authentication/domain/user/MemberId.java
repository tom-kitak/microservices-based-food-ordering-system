package nl.tudelft.sem.group06b.authentication.domain.user;

import java.util.Objects;

/**
 * A DDD value object representing a username in our domain.
 */
public class MemberId {
    private final transient String memberIdValue;

    public MemberId(String memberIdValue) {
        // validate username
        this.memberIdValue = memberIdValue;
    }

    public String getMemberIdValue() {
        return memberIdValue;
    }

    @Override
    public String toString() {
        return memberIdValue;
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
        MemberId otherMemberId = (MemberId) o;
        return Objects.equals(memberIdValue, otherMemberId.memberIdValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberIdValue);
    }
}
