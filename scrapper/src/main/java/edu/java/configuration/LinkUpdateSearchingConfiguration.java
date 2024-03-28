package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.service.LinkService;
import edu.java.service.link_update_searching.searchers.LinkUpdateSearcher;
import edu.java.service.link_update_searching.searchers.github.GithubLinkUpdateSearcher;
import edu.java.service.link_update_searching.searchers.stackoverflow.StackOverFlowLinkUpdateSearcher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LinkUpdateSearchingConfiguration {

    private final GithubClient githubClient;
    private final StackoverflowClient stackoverflowClient;
    private final LinkService linkService;

    @Bean
    public List<LinkUpdateSearcher> linkUpdaterSearcherList() {
        return List.of(
            new GithubLinkUpdateSearcher(githubClient, linkService),
            new StackOverFlowLinkUpdateSearcher(stackoverflowClient, linkService)
        );
    }
}
