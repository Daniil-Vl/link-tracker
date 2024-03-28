package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record LinkUpdate(
    @JsonProperty("id")
    @PositiveOrZero(message = "Id must be non-negative")
    Long id,
    @JsonProperty("url")
    @NotBlank(message = "Url cannot be blank")
    String url,
    @JsonProperty("description")
    @NotBlank(message = "Description cannot be blank")
    String description
) {
}
