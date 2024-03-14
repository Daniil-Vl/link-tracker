package edu.java.bot.client.scrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.ApiErrorResponse;
import edu.java.bot.client.AbstractClientServerTest;
import edu.java.exceptions.ApiErrorException;
import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScrapperClientImplTest extends AbstractClientServerTest {

    private static final String TG_CHAT_ID_HEADER_NAME = "Tg-Chat-Id";
    private static final String TG_CHAT_ENDPOINT = "/tg-chat/%s";
    private static final String LINKS_ENDPOINT = "/links";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ScrapperClient scrapperClient;

    @Override
    protected void initClient() {
        scrapperClient = new ScrapperClientImpl(server.baseUrl());
    }

    @Test
    void givenId_whenRegisterChat_thenReturnOk() {
        Integer chatId = 1;
        String expectedResponseBody = "Chat has been successfully registered";

        server.stubFor(
            post(TG_CHAT_ENDPOINT.formatted(chatId))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedResponseBody)
                )
        );

        String actualResponseBody = scrapperClient.registerChat(chatId);

        assertThat(actualResponseBody).isEqualTo(expectedResponseBody);
    }

    @Test
    void givenInvalidId_whenRegisterChat_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = -1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "400",
            "exception name",
            "exception message",
            List.of()
        );

        server.stubFor(
            post(TG_CHAT_ENDPOINT.formatted(chatId))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.registerChat(chatId))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenId_whenDeleteChat_thenReturnOk() {
        Integer chatId = 1;
        String expectedResponseBody = "Chat has been successfully removed";

        server.stubFor(
            delete(TG_CHAT_ENDPOINT.formatted(chatId))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedResponseBody)
                )
        );

        String actualResponseBody = scrapperClient.deleteChat(chatId);

        assertThat(actualResponseBody).isEqualTo(expectedResponseBody);
    }

    @Test
    void givenInvalidId_whenDeleteChat_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = 1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "400",
            "exception name",
            "exception message",
            List.of()
        );

        server.stubFor(
            delete(TG_CHAT_ENDPOINT.formatted(chatId))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.deleteChat(chatId))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenNonExistentId_whenDeleteChat_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = 1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "404",
            "exception name",
            "exception message",
            List.of()
        );

        server.stubFor(
            delete(TG_CHAT_ENDPOINT.formatted(chatId))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.deleteChat(chatId))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenId_whenGetLinks_thenReturnOk() throws JsonProcessingException {
        Integer chatId = 1;
        ListLinksResponse expectedResponse = new ListLinksResponse(
            List.of(new ListLinksResponse.LinkResponse(1, URI.create("url"))),
            1
        );

        server.stubFor(
            get(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                )
        );

        ListLinksResponse actualResponse = scrapperClient.getLinks(chatId);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenInvalidId_whenGetLinks_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = -1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "400",
            "exception name",
            "exception message",
            List.of()
        );

        server.stubFor(
            get(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.getLinks(chatId))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenNonExistentId_whenGetLinks_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = 1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "404",
            "exception name",
            "exception message",
            List.of()
        );

        server.stubFor(
            get(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.getLinks(chatId))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenId_whenAddLink_ThenReturnOk() throws JsonProcessingException {
        Integer chatId = 1;
        URI url = URI.create("url");
        LinkResponse expectedResponse = new LinkResponse(1, url);
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);

        server.stubFor(
            post(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(addLinkRequest)))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                )
        );

        LinkResponse actualResponse = scrapperClient.addLink(chatId, url);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenInvalidId_whenAddLink_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = 1;
        URI url = URI.create("url");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "400",
            "exception name",
            "exception message",
            List.of()
        );

        server.stubFor(
            post(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(addLinkRequest)))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.addLink(chatId, url))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenId_whenDeleteLinks_thenReturnOk() throws JsonProcessingException {
        Integer chatId = 1;
        URI url = URI.create("url");
        LinkResponse expectedResponse = new LinkResponse(1, url);
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);

        server.stubFor(
            delete(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(removeLinkRequest)))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                )
        );

        LinkResponse actualResponse = scrapperClient.deleteLink(chatId, removeLinkRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenInvalidId_whenDeleteLinks_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = -1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "400",
            "exception name",
            "exception message",
            List.of()
        );
        URI url = URI.create("url");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);

        server.stubFor(
            delete(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(removeLinkRequest)))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.deleteLink(chatId, removeLinkRequest))
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    void givenNonExistentId_whenDeleteLinks_thenThrowApiErrorException() throws JsonProcessingException {
        Integer chatId = 1;
        ApiErrorResponse response = new ApiErrorResponse(
            "description",
            "404",
            "exception name",
            "exception message",
            List.of()
        );
        URI url = URI.create("url");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);

        server.stubFor(
            delete(LINKS_ENDPOINT)
                .withHeader(TG_CHAT_ID_HEADER_NAME, equalTo(chatId.toString()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(removeLinkRequest)))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))
                )
        );

        assertThatThrownBy(() -> scrapperClient.deleteLink(chatId, removeLinkRequest))
            .isInstanceOf(ApiErrorException.class);
    }

}
