package edu.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record LinkUpdateResponse(
    @JsonProperty("id")
    @PositiveOrZero(message = "Id must be non-negative")
    Long id,
    @JsonProperty("url")
    @NotBlank(message = "Url cannot be blank")
    String url,
    @JsonProperty("description")
    @NotBlank(message = "Description cannot be blank")
    String description,
    @JsonProperty("tgChatIds")
    @NotEmpty(message = "List of ids cannot be empty")
    List<Long> ids
) {
}
