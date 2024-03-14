package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.List;

public record ListLinksResponse(
    @JsonProperty("links")
    @NotEmpty(message = "List of links cannot be empty")
    List<LinkResponse> links,
    @JsonProperty("size")
    @Positive(message = "Size must be positive")
    Integer size
) {
    public record LinkResponse(
        @JsonProperty("id")
        @PositiveOrZero(message = "Id must be non-negative")
        Integer id,
        @JsonProperty("url")
        URI url) {
    }
}
