package edu.java.client.github;

import edu.java.dto.github.GithubEventResponse;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClientImpl implements GithubClient {
    private final WebClient webClient;
    private final String accessToken;
    private String baseURL = "https://api.github.com";

    public GithubClientImpl(String baseURL, String accessToken) {
        this.baseURL = baseURL;
        this.accessToken = accessToken;
        this.webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    public GithubClientImpl(String accessToken) {
        this.accessToken = accessToken;
        this.webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    @Override
    public List<GithubEventResponse> getLatestUpdates(String author, String repository, int numberOfUpdates) {
        String endpoint = "/networks/%s/%s/events?per_page=%s".formatted(author, repository, numberOfUpdates);

        List<GithubEventResponse> responses = webClient
            .get()
            .uri(endpoint)
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", "application/json")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<GithubEventResponse>>() {
            })
            .block();

        return responses;
    }
}
