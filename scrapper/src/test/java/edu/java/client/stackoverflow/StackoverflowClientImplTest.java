package edu.java.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
class StackoverflowClientImplTest {
    private WireMockServer server;
    private StackoverflowClient stackoverflowClient;

    @BeforeEach
    void init() {
        server = new WireMockServer();
        server.start();
        stackoverflowClient = new StackoverflowClientImpl(server.baseUrl());
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void givenQuestionId_whenGetQuestion_thenReturnValidStackoverflowUpdateResponse() {
        Long questionId = 1234L;

        String expectedResponseBody = """
            {
              "items": [
                {
                   "tags": [
                     "windows",
                     "c#",
                     ".net"
                   ],
                   "owner": {
                     "reputation": 9001,
                     "user_id": 1,
                     "user_type": "registered",
                     "accept_rate": 55,
                     "profile_image": "https://www.gravatar.com/avatar/a007be5a61f6aa8f3e85ae2fc18dd66e?d=identicon&r=PG",
                     "display_name": "Example User",
                     "link": "https://example.stackexchange.com/users/1/example-user"
                   },
                   "is_answered": false,
                   "view_count": 31415,
                   "favorite_count": 1,
                   "down_vote_count": 2,
                   "up_vote_count": 3,
                   "answer_count": 0,
                   "score": 1,
                   "last_activity_date": 1708675776,
                   "creation_date": 1708632576,
                   "last_edit_date": 1708700976,
                   "question_id": 1234,
                   "link": "https://example.stackexchange.com/questions/1234/an-example-post-title",
                   "title": "An example post title",
                   "body": "An example post body"
                 }
              ],
              "has_more": false,
              "quota_max": 300,
              "quota_remaining": 288
            }
            """;

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
