package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowQuestionAnswersResponse(
    @JsonProperty("items")
    List<Answer> answers
) {
    public record Answer(
        @JsonProperty("owner")
        Owner owner,
        @JsonProperty("last_activity_date")
        OffsetDateTime lastActivityDate,
        @JsonProperty("creation_date")
        OffsetDateTime creationDate
    ) {
        public record Owner(
            @JsonProperty("display_name")
            String name
        ) {
        }
    }
}
