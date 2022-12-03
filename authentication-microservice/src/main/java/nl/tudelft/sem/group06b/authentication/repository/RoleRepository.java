package nl.tudelft.sem.group06b.authentication.repository;

import nl.tudelft.sem.group06b.authentication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
