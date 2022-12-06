package nl.tudelft.sem.template.example.repository;

import nl.tudelft.sem.template.example.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

}
