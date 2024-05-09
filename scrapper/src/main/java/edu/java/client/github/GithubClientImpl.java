package edu.java.client.github;

import edu.java.dto.github.GithubEventResponse;
import edu.java.retrying.RetryFilter;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
public class GithubClientImpl implements GithubClient {
    private final WebClient webClient;
    private String baseURL = "https://api.github.com";

    public GithubClientImpl(String baseURL, String accessToken, RetryFilter retryFilter) {
        this.baseURL = baseURL;
        this.webClient = buildWebClient(baseURL, accessToken, retryFilter);
    }

    public GithubClientImpl(String accessToken, RetryFilter retryFilter) {
        this.webClient = buildWebClient(baseURL, accessToken, retryFilter);
    }

    private WebClient buildWebClient(String baseURL, String accessToken, RetryFilter retryFilter) {
        return WebClient.builder()
            .baseUrl(baseURL)
            .filter(retryFilter)
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
