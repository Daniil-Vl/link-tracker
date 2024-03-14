package edu.java.configuration;

import edu.java.client.bot.BotClient;
import edu.java.client.bot.BotClientImpl;
import edu.java.client.github.GithubClient;
import edu.java.client.github.GithubClientImpl;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.client.stackoverflow.StackoverflowClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ClientConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public BotClient botClient() {
        return new BotClientImpl(
            applicationConfig.api().bot().baseUrl()
        );
    }

    @Bean
    public GithubClient githubClient() {
        return new GithubClientImpl(
            applicationConfig.api().github().baseUrl(),
            applicationConfig.api().github().accessToken()
        );
    }

    @Bean
    public StackoverflowClient stackoverflowClient() {
        return new StackoverflowClientImpl(
            applicationConfig.api().stackoverflow().baseUrl()
        );
    }

}
