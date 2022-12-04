package nl.tudelft.sem.group06b.authentication.domain.user;

import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.EntityEvents;
import nl.tudelft.sem.group06b.authentication.domain.user.events.PasswordWasChangedEvent;
import nl.tudelft.sem.group06b.authentication.domain.user.events.UserWasCreatedEvent;

import javax.persistence.*;
import java.util.Objects;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends EntityEvents {
    /**
     * Identifier for the application user.
     */

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @Convert(converter = UsernameAttributeConverter.class)
    private Username username;

    @Column(name = "password", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    /**
     * An id mapping to the id from the RoleRepository allows for determining the current user's role.
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * Create new application user.
     *
     * @param username The name for the new user
     * @param password The password for the new user
     */
    public User(Username username, HashedPassword password, Long roleId) {
        this.username = username;
        this.password = password;
        this.recordThat(new UserWasCreatedEvent(username));
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    public Username getUsername() {
        return username;
    }

    public HashedPassword getPassword() {
        return password;
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
        return id == (appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
