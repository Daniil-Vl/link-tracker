package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record ListLinksResponse(
    @JsonProperty("links")
    @NotEmpty(message = "List of links cannot be empty")
    List<LinkResponse> links,
    @JsonProperty("size")
    @Positive(message = "Size must be positive")
    Integer size
) {
}
