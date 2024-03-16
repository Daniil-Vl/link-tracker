package edu.java.dto.dao;

import java.time.OffsetDateTime;

public record LinkDto(
    String url,
    OffsetDateTime updatedAt,
    OffsetDateTime lastCheckTime
) {
}
