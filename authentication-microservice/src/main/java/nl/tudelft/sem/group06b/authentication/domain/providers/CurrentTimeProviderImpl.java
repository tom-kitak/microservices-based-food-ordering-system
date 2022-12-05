package nl.tudelft.sem.group06b.authentication.domain.providers;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class CurrentTimeProviderImpl implements CurrentTimeProvider {
    /**
     * Gets current time.
     *
     * @return The current time
     */
    public Instant getCurrentTime() {
        return Instant.now();
    }
}
