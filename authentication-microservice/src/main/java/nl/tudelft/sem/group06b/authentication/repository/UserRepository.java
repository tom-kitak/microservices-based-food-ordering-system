package nl.tudelft.sem.group06b.authentication.repository;

import java.util.Optional;

import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Find user by NetID.
     */
    Optional<User> findByUsername(Username netId);

    /**
     * Check if an existing user already uses a NetID.
     */
    boolean existsByUsername(Username username);
}
