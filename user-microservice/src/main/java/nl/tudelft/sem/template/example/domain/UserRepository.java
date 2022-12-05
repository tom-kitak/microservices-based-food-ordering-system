package nl.tudelft.sem.template.example.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Find user by ID.
     */
    Optional<User> findByMemberId(String memberId);

    /**
     * Check if an existing user already uses a ID.
     */
    boolean existsByMemberId(String memberId);
}
