package edu.java.configuration;

import edu.java.configuration.retrying.BackoffType;
import edu.java.configuration.domain.AccessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Bean
    @NotNull
    Scheduler scheduler,
    @NotNull
    Api api,
    @Bean
    @NotNull
    RateLimit rateLimit,
    Retry retry,
    @NotNull
    AccessType databaseAccessType
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

    public record RateLimit(
        Long capacity,
        Long refillRate,
        Long refillTimeSeconds,
        Long cacheSize,
        Duration expireAfterAccess) {
    }

    public record Retry(Bot bot, Github github, Stackoverflow stackoverflow) {
        public record Bot(
            Integer maxAttempts,
            Duration minBackoff,
            BackoffType backoffType,
            Set<HttpStatus> httpStatuses) {
        }

        public record Github(
            Integer maxAttempts,
            Duration minBackoff,
            BackoffType backoffType,
            Set<HttpStatus> httpStatuses) {
        }

        public record Stackoverflow(
            Integer maxAttempts,
            Duration minBackoff,
            BackoffType backoffType,
            Set<HttpStatus> httpStatuses) {
        }
    }
}
