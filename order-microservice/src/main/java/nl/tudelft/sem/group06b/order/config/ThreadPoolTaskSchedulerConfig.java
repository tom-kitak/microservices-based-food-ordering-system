package nl.tudelft.sem.group06b.order.config;

import nl.tudelft.sem.group06b.order.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan(basePackages = "nl.tudelft.sem.group06b.order", basePackageClasses = {
        Application.class
})
public class ThreadPoolTaskSchedulerConfig {

    /**
     * Thread pool task scheduler thread pool task scheduler.
     *
     * @return the thread pool task scheduler
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }
}
