package edu.java.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotBlank
    String scrapperBaseUrl,
    @NotNull
    Kafka kafka
) {
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
