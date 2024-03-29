package edu.java.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import edu.java.retrying.RetryFilter;
import edu.java.retrying.backoff.ConstantBackoff;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
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
class StackoverflowClientImplTest {
    private final static Path TEST_FILES_PATH = Path.of("src", "test", "resources", "stackoverflow");
    private WireMockServer server;
    private StackoverflowClient stackoverflowClient;

    @BeforeEach
    void init() {
        server = new WireMockServer();
        server.start();
        stackoverflowClient = new StackoverflowClientImpl(
            server.baseUrl(),
            new RetryFilter(
                new ConstantBackoff(Duration.ZERO),
                Set.of(),
                0
            )
        );
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void givenQuestionId_whenGetQuestion_thenReturnValidStackoverflowUpdateResponse() throws IOException {
        Long questionId = 1234L;

        String expectedResponseBody = Files.readString(
            TEST_FILES_PATH.resolve("expected_response_body.json")
        );

        server.stubFor(
            get("/2.3/questions/%s?site=stackoverflow".formatted(questionId))
                .withHeader("Accept", equalTo("application/json")).willReturn(
                    aResponse().withBody(expectedResponseBody).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        );

        StackoverflowQuestionResponse expectedResponse = new StackoverflowQuestionResponse(
            questionId,
            "An example post title",
            OffsetDateTime.of(
                LocalDateTime.ofEpochSecond(1708675776, 0, ZoneOffset.UTC),
                ZoneOffset.UTC
            )
        );

        StackoverflowQuestionResponse actualResponse = stackoverflowClient.getQuestion(questionId);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
