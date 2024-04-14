package edu.java.bot.configuration.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {
    @Bean
    public Counter proccesedMessagesCounter(PrometheusMeterRegistry prometheusMeterRegistry) {
        return prometheusMeterRegistry.counter("messages_processed");
    }
}
