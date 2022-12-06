package nl.tudelft.sem.group06b.coupons.repository;

import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

}
