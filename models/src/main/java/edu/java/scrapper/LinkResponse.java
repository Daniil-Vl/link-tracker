package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record LinkResponse(
    @JsonProperty("id")
    @PositiveOrZero(message = "Id must be non-negative")
    Integer id,
    @JsonProperty("url")
    @NotBlank(message = "Url cannot be blank")
    String url
) {
}
