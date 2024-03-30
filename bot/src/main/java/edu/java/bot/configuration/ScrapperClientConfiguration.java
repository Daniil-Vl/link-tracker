package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.ScrapperClientImpl;
import edu.java.bot.retrying.RetryFilter;
import edu.java.bot.retrying.backoff.Backoff;
import edu.java.bot.retrying.backoff.ConstantBackoff;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ScrapperClientConfiguration {
    private final ApplicationConfig applicationConfig;

    private RetryFilter defaultRetryFilter() {
        return new RetryFilter(
            new ConstantBackoff(Duration.ZERO),
            Set.of(),
            0
        );
    }

    @Bean
    public ScrapperClient scrapperClient() {
        RetryFilter retryFilter;
        ApplicationConfig.RateLimit retryConfig = applicationConfig.rateLimit();

        if (retryConfig == null) {
            retryFilter = defaultRetryFilter();
        } else {
            Backoff backoff = retryConfig.backoffType().getBackoff(retryConfig.minBackoff());
            retryFilter = new RetryFilter(
                backoff,
                retryConfig.httpStatuses(),
                retryConfig.maxAttempts()
            );
        }

        return new ScrapperClientImpl(
            applicationConfig.scrapperBaseUrl(),
            retryFilter
        );
    }
}
