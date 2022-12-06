package nl.tudelft.sem.group06b.authentication.repository;

import nl.tudelft.sem.group06b.authentication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Stores the different roles that a user can have in the system.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
