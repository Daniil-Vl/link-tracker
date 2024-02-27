package edu.java.client.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.github.GithubEventResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
class GithubClientImplTest {
    private final static Path TEST_FILES_PATH = Path.of("src", "test", "resources", "github");
    private WireMockServer server;
    private GithubClient githubClient;

    @BeforeEach
    void init() {
        server = new WireMockServer();
        server.start();
        githubClient = new GithubClientImpl(server.baseUrl(), "github_access_token");
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void givenAuthorAndRepository_whenGetLatestUpdates_thenReturnValidGithubUpdateResponses()
        throws IOException {
        String author = "octocat";
        String repository = "Hello-World";
        int numberOfUpdates = 1;

        String expectedResponseBody = Files.readString(
            TEST_FILES_PATH.resolve("expected_response_body.json")
        );

        server.stubFor(
            get("/networks/%s/%s/events?per_page=%s".formatted(
                author,
                repository,
                numberOfUpdates
            )).withHeader("Accept", equalTo("application/json")).willReturn(
                aResponse().withBody(expectedResponseBody).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );

        GithubEventResponse expectedResponse = new GithubEventResponse(
            22249084964L,
            "PushEvent",
            new GithubEventResponse.Actor(583231L, "octocat"),
            new GithubEventResponse.Repo(1296269L, "octocat/Hello-World"),
            OffsetDateTime.parse("2022-06-09T12:47:28Z")
        );

        GithubEventResponse actualResponse =
            githubClient.getLatestUpdates(author, repository, numberOfUpdates).getFirst();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}
