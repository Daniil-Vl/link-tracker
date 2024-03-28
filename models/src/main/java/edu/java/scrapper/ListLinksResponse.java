package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record ListLinksResponse(
    @JsonProperty("links")
    @NotEmpty(message = "List of links cannot be empty")
    List<LinkResponse> links,
    @JsonProperty("size")
    @PositiveOrZero(message = "Size must be non-negative")
    Integer size
) {
}
