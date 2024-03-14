package edu.java.scrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record RemoveLinkRequest(
    @JsonProperty("link")
    URI link
) {
}
