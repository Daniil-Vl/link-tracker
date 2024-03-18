package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.ScrapperClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ScrapperClientConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClientImpl(
            applicationConfig.scrapperBaseUrl()
        );
    }
}
