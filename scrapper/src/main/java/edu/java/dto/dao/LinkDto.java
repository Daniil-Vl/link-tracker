package edu.java.dto.dao;

import java.time.OffsetDateTime;

public record LinkDto(
    Long id,
    String url,
    OffsetDateTime updatedAt,
    OffsetDateTime lastCheckTime
) {
}
