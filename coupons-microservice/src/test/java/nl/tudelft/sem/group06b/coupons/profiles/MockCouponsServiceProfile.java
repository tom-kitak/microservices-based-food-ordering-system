package nl.tudelft.sem.group06b.coupons.profiles;

import nl.tudelft.sem.group06b.coupons.service.CouponsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * A configuration profile to allow injection of a mock CouponsService.
 */
@Profile("mockCouponsService")
@Configuration
public class MockCouponsServiceProfile {

    /**
     * Mocks the CouponsService.
     *
     * @return a mocked CouponsService
     */
    @Bean
    @Primary
    public CouponsService getCouponsService() {
        return Mockito.mock(CouponsService.class);
    }
}
