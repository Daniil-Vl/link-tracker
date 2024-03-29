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
    private final ApplicationConfig applicationConfig;
    private Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private Bandwidth basicBandwidth;

    public Bucket getBucket(String ip) {
        return cache.computeIfAbsent(ip, address -> buildNewBucket());
    }

    @PostConstruct
    void initBandWidth() {
        basicBandwidth = Bandwidth.classic(
            applicationConfig.rateLimit().capacity(),
            Refill.greedy(
                applicationConfig.rateLimit().refillRate(),
                Duration.ofSeconds(applicationConfig.rateLimit().refillTimeSeconds())
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
