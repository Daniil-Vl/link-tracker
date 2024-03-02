package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubEventResponse(
    Long id,
    String type,
    Actor actor,
    Repo repo,
    @JsonProperty("created_at")
    OffsetDateTime createdAt
) {
    public record Actor(Long id, String login) {
    }

    public record Repo(Long id, String name) {
    }
}
