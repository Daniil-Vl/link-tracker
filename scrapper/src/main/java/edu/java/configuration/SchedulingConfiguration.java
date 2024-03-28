package edu.java.configuration;

import edu.java.scheduling.LinkUpdaterScheduler;
import edu.java.service.LinkUpdaterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    @Bean
    @ConditionalOnProperty(value = "app.scheduler.enable", matchIfMissing = false)
    public LinkUpdaterScheduler linkUpdaterScheduler(LinkUpdaterService linkUpdaterService) {
        return new LinkUpdaterScheduler(linkUpdaterService);
    }
}
