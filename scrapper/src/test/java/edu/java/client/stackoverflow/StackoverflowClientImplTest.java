package edu.java.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.stackoverflow.StackoverflowQuestionAnswersResponse;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
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
        server = new WireMockServer(56789);
        server.start();
        stackoverflowClient = new StackoverflowClientImpl(server.baseUrl());
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
                .withHeader("Accept", equalTo(APPLICATION_JSON_VALUE))
                .willReturn(
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

    @Test
    void temp() throws IOException {
        Long questionId = 1L;

        String expectedResponseBody = Files.readString(
            TEST_FILES_PATH.resolve("expected_answers_response_body.json")
        );

        server.stubFor(
            get("/2.3/questions/%s/answers?site=stackoverflow".formatted(questionId))
                .withHeader("Accept", equalTo(APPLICATION_JSON_VALUE))
                .willReturn(
                    aResponse().withBody(expectedResponseBody).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        );

        StackoverflowQuestionAnswersResponse expectedResponse = new StackoverflowQuestionAnswersResponse(
            List.of(
                new StackoverflowQuestionAnswersResponse.Answer(
                    new StackoverflowQuestionAnswersResponse.Answer.Owner("First_user"),
                    OffsetDateTime.parse("2021-09-11T17:49:33Z"),
                    OffsetDateTime.parse("2021-09-11T16:17:21Z")
                ),

                new StackoverflowQuestionAnswersResponse.Answer(
                    new StackoverflowQuestionAnswersResponse.Answer.Owner("Second_user"),
                    OffsetDateTime.parse("2021-09-11T15:59:38Z"),
                    OffsetDateTime.parse("2021-09-11T15:59:38Z")
                )
            )
        );

        StackoverflowQuestionAnswersResponse actualResponse = stackoverflowClient.getAnswers(questionId);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
