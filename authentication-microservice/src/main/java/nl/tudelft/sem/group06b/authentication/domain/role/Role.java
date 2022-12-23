package nl.tudelft.sem.group06b.authentication.domain.role;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
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
    /**
     * Identifier for the application role.
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roleName", nullable = false, unique = true)
    @Convert(converter = RoleNameAttributeConverter.class)
    private RoleName roleName;

    /**
     * Create new application role.
     *
     * @param roleName The name of the new application role
     */
    public Role(RoleName roleName) {
        this.roleName = roleName;
        this.recordThat(new RoleWasCreatedEvent(roleName));
    }

    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return roleName;
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
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }
}
