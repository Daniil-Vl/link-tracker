package edu.java.client.github;

import edu.java.dto.github.GithubEventResponse;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
public class GithubClientImpl implements GithubClient {
    private final WebClient webClient;
    private final String accessToken;
    private String baseURL = "https://api.github.com";

    public GithubClientImpl(String baseURL, String accessToken) {
        this.baseURL = baseURL;
        this.accessToken = accessToken;
        this.webClient = buildWebClient(baseURL, accessToken);
    }

    public GithubClientImpl(String accessToken) {
        this.accessToken = accessToken;
        this.webClient = buildWebClient(baseURL, accessToken);
    }

    private WebClient buildWebClient(String baseURL, String accessToken) {
        return WebClient.builder()
            .baseUrl(baseURL)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setBearerAuth(accessToken);
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            })
            .build();
    }

    @Override
    public List<GithubEventResponse> getLatestUpdates(String author, String repository, int numberOfUpdates) {
        String endpoint = "/networks/%s/%s/events?per_page=%s".formatted(author, repository, numberOfUpdates);

        return webClient
            .get()
            .uri(endpoint)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<GithubEventResponse>>() {
            })
            .block();
    }
}
