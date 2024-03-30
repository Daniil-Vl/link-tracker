package edu.java.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import edu.java.configuration.ApplicationConfig;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfiguration {
    @Bean
    public Caffeine caffeineConfig(ApplicationConfig applicationConfig) {
        return Caffeine.newBuilder()
            .expireAfterAccess(applicationConfig.rateLimit().expireAfterAccess())
            .maximumSize(applicationConfig.rateLimit().cacheSize());
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
