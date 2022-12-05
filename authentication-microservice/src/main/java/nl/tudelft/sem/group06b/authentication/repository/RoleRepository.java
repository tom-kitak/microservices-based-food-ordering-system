package nl.tudelft.sem.group06b.authentication.repository;

import java.util.Optional;
import nl.tudelft.sem.group06b.authentication.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting role aggregate roots.
 */
@Repository
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
     * Check if an existing role with the same name.
     */
    boolean existsByName(String roleName);
}
