package nl.tudelft.sem.group06b.authentication.domain.user;

import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.EntityEvents;
import nl.tudelft.sem.group06b.authentication.domain.user.events.UserWasCreatedEvent;

import javax.persistence.*;

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

}
