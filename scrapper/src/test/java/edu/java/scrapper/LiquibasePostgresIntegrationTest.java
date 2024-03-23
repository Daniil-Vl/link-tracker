package edu.java.scrapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.assertThat;

public class LiquibasePostgresIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testLinkTable() {
        String linkUrl = "url";
        OffsetDateTime expectedUpdatedAt = OffsetDateTime.of(2024, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);
        OffsetDateTime expectedLastCheckTime = OffsetDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);

        jdbcTemplate.update(
            "INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?)",
            linkUrl,
            expectedUpdatedAt,
            expectedLastCheckTime
        );

        OffsetDateTime actualUpdatedAt = jdbcTemplate.queryForObject(
            "SELECT updated_at FROM link WHERE url = %s".formatted(linkUrl),
            OffsetDateTime.class
        );

        OffsetDateTime actualLastCheckTime = jdbcTemplate.queryForObject(
            "SELECT last_check_time FROM link WHERE url = %s".formatted(linkUrl),
            OffsetDateTime.class
        );

        assertThat(actualUpdatedAt).isEqualTo(expectedUpdatedAt);
        assertThat(actualLastCheckTime).isEqualTo(expectedLastCheckTime);
    }

    @Test
    void testChatTable() {
        Long expectedChatId = 1L;

        jdbcTemplate.update(
            "INSERT INTO chat(chat_id) VALUES (?)",
            expectedChatId
        );

        Long actualChatId = jdbcTemplate.queryForObject(
            "SELECT chat_id FROM chat WHERE chat_id = %s".formatted(expectedChatId),
            Long.class
        );

        assertThat(actualChatId).isEqualTo(expectedChatId);
    }
}
