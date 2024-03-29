package edu.java.configuration.retrying;

import edu.java.configuration.ApplicationConfig;
import edu.java.retrying.RetryFilter;
import edu.java.retrying.backoff.Backoff;
import edu.java.retrying.backoff.ConstantBackoff;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig applicationConfig;

    private RetryFilter defaultRetryFilter() {
        return new RetryFilter(
            new ConstantBackoff(Duration.ZERO),
            Set.of(),
            0
        );
    }

    @Bean
    public RetryFilter botRetryFilter() {
        if (applicationConfig.retry() == null || applicationConfig.retry().bot() == null) {
            return defaultRetryFilter();
        }

        ApplicationConfig.Retry.Bot retryConfig = applicationConfig.retry().bot();
        Backoff backoff = retryConfig.backoffType().getBackoff(retryConfig.minBackoff());

        return new RetryFilter(
            backoff,
            retryConfig.httpStatuses(),
            retryConfig.maxAttempts()
        );
    }

    @Bean
    public RetryFilter githubRetryFilter() {
        if (applicationConfig.retry() == null || applicationConfig.retry().github() == null) {
            return defaultRetryFilter();
        }

        ApplicationConfig.Retry.Github retryConfig = applicationConfig.retry().github();
        Backoff backoff = retryConfig.backoffType().getBackoff(retryConfig.minBackoff());

        return new RetryFilter(
            backoff,
            retryConfig.httpStatuses(),
            retryConfig.maxAttempts()
        );
    }

    @Bean
    public RetryFilter stackoverflowRetryFilter() {
        if (applicationConfig.retry() == null || applicationConfig.retry().stackoverflow() == null) {
            return defaultRetryFilter();
        }

        ApplicationConfig.Retry.Stackoverflow retryConfig = applicationConfig.retry().stackoverflow();
        Backoff backoff = retryConfig.backoffType().getBackoff(retryConfig.minBackoff());

        return new RetryFilter(
            backoff,
            retryConfig.httpStatuses(),
            retryConfig.maxAttempts()
        );
    }
}
