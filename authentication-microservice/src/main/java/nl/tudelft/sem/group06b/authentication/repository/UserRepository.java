package nl.tudelft.sem.group06b.authentication.repository;

import nl.tudelft.sem.group06b.authentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
