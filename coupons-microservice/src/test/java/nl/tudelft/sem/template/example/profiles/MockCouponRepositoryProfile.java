package nl.tudelft.sem.template.example.profiles;

import nl.tudelft.sem.template.example.repository.CouponRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * A configuration profile to allow injection of a mock CouponRepository.
 */
@Profile("mockCouponRepository")
@Configuration
public class MockCouponRepositoryProfile {

    /**
     * Mocks the CouponRepository.
     *
     * @return a mocked CouponRepository
     */
    @Bean
    @Primary
    public CouponRepository getMockCouponRepository() {
        return Mockito.mock(CouponRepository.class);
    }
}
