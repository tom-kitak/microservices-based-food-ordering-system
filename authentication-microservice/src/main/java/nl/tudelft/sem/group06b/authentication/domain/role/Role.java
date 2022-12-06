package nl.tudelft.sem.group06b.authentication.domain.role;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.EntityEvents;
import nl.tudelft.sem.group06b.authentication.domain.role.events.RoleWasCreatedEvent;

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

    public String getName() {
        return name;
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
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
