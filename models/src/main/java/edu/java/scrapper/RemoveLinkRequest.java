package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(
    @JsonProperty("link")
    @NotBlank(message = "Link cannot be blank")
    String link
) {
}
