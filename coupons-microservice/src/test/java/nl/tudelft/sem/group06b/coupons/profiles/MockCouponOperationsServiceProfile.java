package nl.tudelft.sem.group06b.coupons.profiles;

import nl.tudelft.sem.group06b.coupons.service.operations.CouponOperationsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * A configuration profile to allow injection of a mock CouponOperationsService.
 */
@Profile("mockCouponOperationsService")
@Configuration
public class MockCouponOperationsServiceProfile {
    /**
     * Mocks the CouponsService.
     *
     * @return a mocked CouponsService
     */
    @Bean
    @Primary
    public CouponOperationsService getCouponsService() {
        return Mockito.mock(CouponOperationsService.class);
    }
}
