package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.resource.ResourceManager;
import edu.java.resource.github.GithubResourceManager;
import edu.java.resource.stackoverflow.StackOverFlowResourceManager;
import edu.java.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ResourceManagerConfiguration {

    private final GithubClient githubClient;
    private final StackoverflowClient stackoverflowClient;
    private final LinkService linkService;

    @Bean
    public List<ResourceManager> resourceManagerList() {
        return List.of(
            new GithubResourceManager(githubClient, linkService),
            new StackOverFlowResourceManager(stackoverflowClient, linkService)
        );
    }
}
