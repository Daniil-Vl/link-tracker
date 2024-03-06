package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import java.net.URI;

public record LinkResponse(
    @JsonProperty("id")
    @PositiveOrZero(message = "Id must be non-negative")
    Integer id,
    @JsonProperty("url")
    URI url
) {
}
