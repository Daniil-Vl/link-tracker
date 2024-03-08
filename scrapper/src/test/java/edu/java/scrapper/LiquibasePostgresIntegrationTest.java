package edu.java.scrapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LiquibasePostgresIntegrationTest extends IntegrationTest {
    @Test
    void initTest() {
        Integer expectedChatId = 1;
        Integer expectedLinkId = 1;
        String expectedLinkUrl = "url";
        Timestamp expectedLinkDate = Timestamp.valueOf(LocalDateTime.of(2024, 3, 8, 16, 0, 0, 0));

        jdbcTemplate.update(
            "INSERT INTO chat (id, chat_id) VALUES (?, ?)",
            expectedChatId,
            expectedChatId
        );
        jdbcTemplate.update(
            "INSERT INTO link (id, url, updated_at) VALUES (?, ?, ?)",
            expectedLinkId,
            expectedLinkUrl,
            expectedLinkDate
        );
        jdbcTemplate.update(
            "INSERT INTO chats_links (chat_id, link_id) VALUES (?, ?)",
            expectedChatId,
            expectedLinkId
        );

        Integer actualChatId = jdbcTemplate.queryForObject(
            "SELECT chat_id FROM chat WHERE id = 1",
            Integer.class
        );
        Integer actualLinkId = jdbcTemplate.queryForObject(
            "SELECT id FROM link WHERE id = 1",
            Integer.class
        );
        String actualLinkUrl = jdbcTemplate.queryForObject(
            "SELECT url FROM link WHERE id = 1",
            String.class
        );
        Timestamp actualLinkDate =
            jdbcTemplate.queryForObject(
                "SELECT updated_at FROM link WHERE id = 1",
                Timestamp.class
            );
        Integer actualChatId2 =
            jdbcTemplate.queryForObject(
                "SELECT chat_id FROM chats_links WHERE link_id = 1",
                Integer.class
            );

        assertThat(actualChatId).isEqualTo(expectedChatId);
        assertThat(actualLinkId).isEqualTo(expectedLinkId);
        assertThat(actualLinkUrl).isEqualTo(expectedLinkUrl);
        assertThat(actualLinkDate).isEqualTo(expectedLinkDate);
        assertThat(actualChatId2).isEqualTo(expectedChatId);
    }
}
