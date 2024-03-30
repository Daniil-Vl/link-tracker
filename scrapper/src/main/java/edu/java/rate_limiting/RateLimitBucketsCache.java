package edu.java.rate_limiting;

import edu.java.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimitBucketsCache {
    private final ApplicationConfig.RateLimit rateLimit;
    private Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private Bandwidth basicBandwidth;

    public Bucket getBucket(String ip) {
        return cache.computeIfAbsent(ip, address -> buildNewBucket());
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
