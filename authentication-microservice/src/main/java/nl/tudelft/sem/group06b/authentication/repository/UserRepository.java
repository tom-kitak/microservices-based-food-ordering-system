package nl.tudelft.sem.group06b.authentication.repository;

import nl.tudelft.sem.group06b.authentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Stores all the users that are in the system with their encrypted authentication credentials.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
