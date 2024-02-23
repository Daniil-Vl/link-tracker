package edu.java.client.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.github.GithubEventResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
class GithubClientImplTest {
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
    void givenAuthorAndRepository_whenGetLatestUpdates_thenReturnValidGithubUpdateResponses() {
        String author = "octocat";
        String repository = "Hello-World";
        int numberOfUpdates = 1;

        String expectedResponseBody = """
            [
              {
                "id": "22249084964",
                "type": "PushEvent",
                "actor": {
                  "id": 583231,
                  "login": "octocat",
                  "display_login": "octocat",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/octocat",
                  "avatar_url": "https://avatars.githubusercontent.com/u/583231?v=4"
                },
                "repo": {
                  "id": 1296269,
                  "name": "octocat/Hello-World",
                  "url": "https://api.github.com/repos/octocat/Hello-World"
                },
                "payload": {
                  "push_id": 10115855396,
                  "size": 1,
                  "distinct_size": 1,
                  "ref": "refs/heads/master",
                  "head": "7a8f3ac80e2ad2f6842cb86f576d4bfe2c03e300",
                  "before": "883efe034920928c47fe18598c01249d1a9fdabd",
                  "commits": [
                    {
                      "sha": "7a8f3ac80e2ad2f6842cb86f576d4bfe2c03e300",
                      "author": {
                        "email": "octocat@github.com",
                        "name": "Monalisa Octocat"
                      },
                      "message": "commit",
                      "distinct": true,
                      "url": "https://api.github.com/repos/octocat/Hello-World/commits/7a8f3ac80e2ad2f6842cb86f576d4bfe2c03e300"
                    }
                  ]
                },
                "public": true,
                "created_at": "2022-06-09T12:47:28Z"
              },
              {
                "id": "22237752260",
                "type": "WatchEvent",
                "actor": {
                  "id": 583231,
                  "login": "octocat",
                  "display_login": "octocat",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/rrubenich",
                  "avatar_url": "https://avatars.githubusercontent.com/u/583231?v=4"
                },
                "repo": {
                  "id": 1296269,
                  "name": "octocat/Hello-World",
                  "url": "https://api.github.com/repos/octocat/Hello-World"
                },
                "payload": {
                  "action": "started"
                },
                "public": true,
                "created_at": "2022-06-08T23:29:25Z"
              }
            ]
            """;

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

        GithubEventResponse actualResponse = githubClient.getLatestUpdates(author, repository, numberOfUpdates).getFirst();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}
