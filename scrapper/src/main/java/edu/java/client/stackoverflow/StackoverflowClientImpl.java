package edu.java.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.dto.stackoverflow.StackoverflowQuestionAnswersResponse;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import edu.java.retrying.RetryFilter;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClientImpl implements StackoverflowClient {
    private final WebClient webClient;
    private String baseURL = "https://api.stackexchange.com";

    public StackoverflowClientImpl(String baseURL, RetryFilter retryFilter) {
        this.baseURL = baseURL;
        this.webClient = buildWebClient(baseURL, retryFilter);
    }

    public StackoverflowClientImpl(RetryFilter retryFilter) {
        this.webClient = buildWebClient(baseURL, retryFilter);
    }

    private WebClient buildWebClient(String baseURL, RetryFilter retryFilter) {
        return WebClient.builder()
            .baseUrl(baseURL)
            .filter(retryFilter)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            })
            .build();
    }

    @Override
    public StackoverflowQuestionResponse getQuestion(Long questionId) {
        String endpoint = "/2.3/questions/%s?site=stackoverflow".formatted(questionId);

        Response response = webClient
            .get()
            .uri(endpoint)
            .retrieve()
            .bodyToMono(Response.class)
            .block();

        Question question = response.questions().getFirst();

        return new StackoverflowQuestionResponse(
            question.id(),
            question.title(),
            question.lastActivityDate()
        );
    }

    @Override
    public StackoverflowQuestionAnswersResponse getAnswers(Long questionId) {
        String endpoint = "2.3/questions/%s/answers?site=stackoverflow".formatted(questionId);

        return webClient
            .get()
            .uri(endpoint)
            .retrieve()
            .bodyToMono(StackoverflowQuestionAnswersResponse.class)
            .block();
    }

    record Question(
        @JsonProperty("question_id")
        Long id,
        @JsonProperty("title")
        String title,
        @JsonProperty("last_activity_date")
        OffsetDateTime lastActivityDate) {
    }

    record Response(
        @JsonProperty("items")
        List<Question> questions
    ) {
    }
}
