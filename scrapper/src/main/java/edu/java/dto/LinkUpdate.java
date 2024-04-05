package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Dto to represent resource update
 *
 * @param id          - id of resource in db
 * @param url         - resource url
 * @param description - description of resource update
 */
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
