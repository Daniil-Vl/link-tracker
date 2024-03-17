package edu.java.client.bot;

import edu.java.ApiErrorResponse;
import edu.java.LinkUpdateResponse;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
public class BotClientImpl implements BotClient {
    private static final String ENDPOINT = "/updates";
    private final WebClient webClient;
    private String baseUrl = "http://localhost:8090";

    public BotClientImpl() {
        this.webClient = buildWebClient(baseUrl);
    }

    public BotClientImpl(String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = buildWebClient(baseUrl);
    }

    private WebClient buildWebClient(String baseUrl) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            })
            .build();
    }

    @Override
    public String sendLinkUpdateRequest(LinkUpdateResponse linkUpdateResponse) {
        String response = this.webClient
            .post()
            .uri(ENDPOINT)
            .bodyValue(linkUpdateResponse)
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                clientResponse -> {
                    log.error("Failed to execute link update request");
                    return clientResponse
                        .bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> {
                            return new IllegalArgumentException(apiErrorResponse.exceptionMessage());
                        });
                }
            )
            .bodyToMono(String.class)
            .block();

        log.info(response);
        return response;
    }
}
