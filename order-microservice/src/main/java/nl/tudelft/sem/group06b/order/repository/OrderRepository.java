package nl.tudelft.sem.group06b.order.repository;

import nl.tudelft.sem.group06b.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

}
