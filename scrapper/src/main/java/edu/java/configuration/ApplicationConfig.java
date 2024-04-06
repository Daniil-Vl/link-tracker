package edu.java.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    Api api,
    @NotNull
    Kafka kafka,
    @NotNull
    Boolean useQueue
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

    public record Kafka(
        @NotBlank
        String bootstrapServers,
        @NotBlank
        String groupId,
        @NotBlank
        String topicName,
        @Positive
        Integer partitions,
        @Positive
        Integer replicationFactor) {
    }
}
