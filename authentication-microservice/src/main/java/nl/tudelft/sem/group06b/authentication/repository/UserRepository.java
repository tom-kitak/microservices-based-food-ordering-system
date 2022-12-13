package nl.tudelft.sem.group06b.authentication.repository;

import java.util.Optional;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by memberId.
     */
    Optional<User> findByMemberId(MemberId memberId);

    /**
     * Check if an existing user already uses the same memberId.
     */
    boolean existsByMemberId(MemberId username);
}
