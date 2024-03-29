package edu.java.configuration;

import edu.java.client.bot.BotClient;
import edu.java.client.bot.BotClientImpl;
import edu.java.client.github.GithubClient;
import edu.java.client.github.GithubClientImpl;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.client.stackoverflow.StackoverflowClientImpl;
import edu.java.retrying.RetryFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ClientConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public BotClient botClient(RetryFilter botRetryFilter) {
        return new BotClientImpl(
            applicationConfig.api().bot().baseUrl(),
            botRetryFilter
        );
    }

    @Bean
    public GithubClient githubClient(RetryFilter githubRetryFilter) {
        return new GithubClientImpl(
            applicationConfig.api().github().baseUrl(),
            applicationConfig.api().github().accessToken(),
            githubRetryFilter
        );
    }

    @Bean
    public StackoverflowClient stackoverflowClient(RetryFilter stackoverflowRetryFilter) {
        return new StackoverflowClientImpl(
            applicationConfig.api().stackoverflow().baseUrl(),
            stackoverflowRetryFilter
        );
    }

}
