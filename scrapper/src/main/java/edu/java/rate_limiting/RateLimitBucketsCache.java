package edu.java.rate_limiting;

import edu.java.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimitBucketsCache {
    private final ApplicationConfig.RateLimit rateLimit;
    private Bandwidth basicBandwidth;

    @Cacheable(value = "rate-limit-buckets-cache", key = "#root.args[0]")
    public Bucket getBucket(String ip) {
        return buildNewBucket();
    }

    @PostConstruct
    void initBandWidth() {
        basicBandwidth = Bandwidth.classic(
            rateLimit.capacity(),
            Refill.greedy(
                rateLimit.refillRate(),
                Duration.ofSeconds(rateLimit.refillTimeSeconds())
            )
        );
    }

    private Bucket buildNewBucket() {
        return Bucket
            .builder()
            .addLimit(basicBandwidth)
            .build();
    }
}
