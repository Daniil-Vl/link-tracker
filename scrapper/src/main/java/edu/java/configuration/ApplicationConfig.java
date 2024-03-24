package edu.java.configuration;

import edu.java.configuration.domain.AccessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Bean
    @NotNull
    Scheduler scheduler,
    @NotNull
    AccessType databaseAccessType,
    @NotNull
    Api api
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Api(@NotNull Bot bot, @NotNull Github github, @NotNull Stackoverflow stackoverflow) {
        public record Bot(@NotBlank String baseUrl) {
        }

        public record Github(@NotBlank String baseUrl, @NotBlank String accessToken) {
        }

        public record Stackoverflow(@NotBlank String baseUrl) {
        }
    }
}
