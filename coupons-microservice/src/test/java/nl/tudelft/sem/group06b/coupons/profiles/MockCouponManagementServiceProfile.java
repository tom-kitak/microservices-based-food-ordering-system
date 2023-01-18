package nl.tudelft.sem.group06b.coupons.profiles;

import nl.tudelft.sem.group06b.coupons.service.management.CouponManagementService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * A configuration profile to allow injection of a mock CouponManagementService.
 */
@Profile("mockCouponManagementService")
@Configuration
public class MockCouponManagementServiceProfile {
    @Bean
    @Primary
    public CouponManagementService getCouponManagementService() {
        return Mockito.mock(CouponManagementService.class);
    }
}



