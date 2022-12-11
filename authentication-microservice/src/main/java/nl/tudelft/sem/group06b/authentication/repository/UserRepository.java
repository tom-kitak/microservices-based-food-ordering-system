package nl.tudelft.sem.group06b.authentication.repository;

import java.util.Optional;
import nl.tudelft.sem.group06b.authentication.domain.user.User;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username.
     */
    Optional<User> findByMemberID(MemberID username);

    /**
     * Check if an existing user already uses a NetID.
     */
    boolean existsByMemberID(MemberID username);
}
