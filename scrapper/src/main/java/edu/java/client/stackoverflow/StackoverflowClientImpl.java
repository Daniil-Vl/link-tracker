package edu.java.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClientImpl implements StackoverflowClient {
    private final WebClient webClient;
    private String baseURL = "https://api.stackexchange.com";

    public StackoverflowClientImpl(String baseURL) {
        this.baseURL = baseURL;
        this.webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    public StackoverflowClientImpl() {
        this.webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    @Override
    public StackoverflowQuestionResponse getQuestion(Long questionId) {
        String endpoint = "/2.3/questions/%s?site=stackoverflow".formatted(questionId);

        Response response = webClient
            .get()
            .uri(endpoint)
            .header("Accept", "application/json")
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

    record Question(Long id, String title, OffsetDateTime lastActivityDate) {
    }

    record Response(
        @JsonProperty("items")
        @JsonDeserialize(contentUsing = QuestionDeserializer.class)
        List<Question> questions
    ) {
    }

    /**
     * Custom Deserializer use in parsing a list of questions
     */
    static class QuestionDeserializer extends JsonDeserializer<Question> {
        QuestionDeserializer() {
        }

        @Override
        public Question deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            long questionId = node.get("question_id").asLong();
            String title = node.get("title").asText();
            long lastActivityDateEpochSeconds = node.get("last_activity_date").asLong();
            OffsetDateTime time = OffsetDateTime.of(
                LocalDateTime.ofEpochSecond(lastActivityDateEpochSeconds, 0, ZoneOffset.UTC),
                ZoneOffset.UTC
            );

            return new Question(questionId, title, time);
        }
    }
}
