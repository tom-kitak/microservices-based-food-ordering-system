package nl.tudelft.sem.group06b.coupons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Example microservice application.
 */
@SpringBootApplication
@EnableJpaRepositories("nl.tudelft.sem.group06b.coupons.repository")
public class CouponsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponsServiceApplication.class, args);
    }
}
