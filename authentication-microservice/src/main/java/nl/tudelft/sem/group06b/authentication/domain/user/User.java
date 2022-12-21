package nl.tudelft.sem.group06b.authentication.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.EntityEvents;
import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.user.events.PasswordWasChangedEvent;
import nl.tudelft.sem.group06b.authentication.domain.user.events.RoleWasChangedEvent;
import nl.tudelft.sem.group06b.authentication.domain.user.events.UserWasCreatedEvent;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class User extends EntityEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "memberID", nullable = false, unique = true)
    @Convert(converter = MemberIdAttributeConverter.class)
    private MemberId memberId;

    @Column(name = "password", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    /**
     * An id mapping to the id from the RoleRepository allows for determining the current user's role.
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * Create new application user.
     *
     * @param memberId The name for the new user
     * @param password The password for the new user
     */
    public User(MemberId memberId, HashedPassword password, Role role) {
        this.memberId = memberId;
        this.password = password;
        this.role = role;
        this.recordThat(new UserWasCreatedEvent(memberId));
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
        this.recordThat(new RoleWasChangedEvent(this));
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
        User appUser = (User) o;
        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}
