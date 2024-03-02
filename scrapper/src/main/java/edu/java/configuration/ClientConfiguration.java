package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.github.GithubClientImpl;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.client.stackoverflow.StackoverflowClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Autowired
    private ApplicationConfig applicationConfig;

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
