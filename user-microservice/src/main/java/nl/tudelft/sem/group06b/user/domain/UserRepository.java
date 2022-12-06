package nl.tudelft.sem.group06b.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Find user by memberId.
     */
    Optional<User> findByMemberId(String memberId);

    /**
     * Check if an existing user already uses a memberId.
     */
    boolean existsByMemberId(String memberId);
}
