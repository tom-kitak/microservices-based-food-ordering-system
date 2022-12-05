package nl.tudelft.sem.group06b.authentication.repository;

import java.util.Optional;

import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username.
     */
    Optional<User> findByUsername(Username username);

    /**
     * Check if an existing user already uses a NetID.
     */
    boolean existsByUsername(Username username);
}
