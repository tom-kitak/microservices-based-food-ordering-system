package nl.tudelft.sem.group06b.authentication.repository;

import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find role by roleName.
     */
    Optional<Role> findByName(String roleName);

    /**
     * Find role by id.
     */
    Optional<Role> findById(Long id);

    /**
     * Check if an existing role with the same name
     */
    boolean existsByName(String roleName);
}
