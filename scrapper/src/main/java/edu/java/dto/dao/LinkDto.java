package edu.java.dto.dao;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkDto(
    Long id,
    URI url,
    OffsetDateTime updatedAt,
    OffsetDateTime lastCheckTime
) {
}
