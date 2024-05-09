package edu.java.configuration.rate_limiting;

import edu.java.rate_limiting.RateLimitBucketsCache;
import edu.java.rate_limiting.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfiguration implements WebMvcConfigurer {
    private final RateLimitBucketsCache rateLimitBucketsCache;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(new RateLimitInterceptor(rateLimitBucketsCache))
            .addPathPatterns("/**");
    }
}
