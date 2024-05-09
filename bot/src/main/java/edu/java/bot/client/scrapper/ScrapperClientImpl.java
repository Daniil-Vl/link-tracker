package edu.java.bot.client.scrapper;

import edu.java.ApiErrorResponse;
import edu.java.bot.retrying.RetryFilter;
import edu.java.exceptions.ApiErrorException;
import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
public class ScrapperClientImpl implements ScrapperClient {
    private static final String TG_CHAT_ID_HEADER_NAME = "Tg-Chat-Id";
    private static final String TG_CHAT_ENDPOINT = "/tg-chat/%s";
    private static final String LINKS_ENDPOINT = "/links";
    private static final List<HttpStatus> API_ERROR_RESPONSE_HTTP_CODES = List.of(
        HttpStatus.NOT_FOUND,
        HttpStatus.BAD_REQUEST
    );
    private final WebClient webClient;
    private String baseUrl = "http://localhost:8080";

    public ScrapperClientImpl(String baseUrl, RetryFilter retryFilter) {
        this.baseUrl = baseUrl;
        this.webClient = buildWebClient(baseUrl, retryFilter);
    }

    public ScrapperClientImpl(RetryFilter retryFilter) {
        this.webClient = buildWebClient(baseUrl, retryFilter);
    }

    private WebClient buildWebClient(String baseUrl, RetryFilter retryFilter) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .filter(retryFilter)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            })
            .defaultStatusHandler(
                httpStatusCode -> API_ERROR_RESPONSE_HTTP_CODES.stream()
                    .anyMatch(httpStatus -> httpStatus.isSameCodeAs(httpStatusCode)),
                clientResponse ->
                    clientResponse
                        .bodyToMono(ApiErrorResponse.class)
                        .map(ApiErrorException::new)
            )
            .build();
    }

    @Override
    public String registerChat(Long id) {
        log.info("Register chat request starts");
        String endpoint = TG_CHAT_ENDPOINT.formatted(id);

        String response = webClient
            .post()
            .uri(endpoint)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.info("Register chat request successfully completed");
        return response;
    }

    @Override
    public String deleteChat(Long id) {
        log.info("Delete chat request starts");
        String endpoint = TG_CHAT_ENDPOINT.formatted(id);

        String response = webClient
            .delete()
            .uri(endpoint)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.info("Delete chat request successfully completed");
        return response;
    }

    @Override
    public ListLinksResponse getLinks(Long tgChatId) {
        log.info("Get links request starts");

        ListLinksResponse response = webClient
            .get()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER_NAME, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();

        log.info("Get links request successfully completed");
        return response;
    }

    @Override
    public LinkResponse addLink(Long tgChatId, URI link) {
        log.info("Add link request starts");
        AddLinkRequest body = new AddLinkRequest(link);

        LinkResponse response = webClient
            .post()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER_NAME, tgChatId.toString())
            .bodyValue(body)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();

        log.info("Add links request successfully completed");
        return response;
    }

    @Override
    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Delete link request starts");

        LinkResponse response = webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER_NAME, tgChatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();

        log.info("Delete links request successfully completed");
        return response;
    }
}
