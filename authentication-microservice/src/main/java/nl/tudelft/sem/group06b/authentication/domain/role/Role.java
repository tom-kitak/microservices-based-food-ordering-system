package nl.tudelft.sem.group06b.authentication.domain.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.EntityEvents;
import nl.tudelft.sem.group06b.authentication.domain.role.events.RoleWasCreatedEvent;
import nl.tudelft.sem.group06b.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import nl.tudelft.sem.group06b.authentication.domain.user.events.PasswordWasChangedEvent;
import nl.tudelft.sem.group06b.authentication.domain.user.events.UserWasCreatedEvent;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role extends EntityEvents {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Create new application user.
     *
     * @param roleName The name of the new application role
     */
    public Role(String roleName) {
        this.name = roleName;
        this.recordThat(new RoleWasCreatedEvent(roleName));
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
        Role role = (Role) o;
        return id == (role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
