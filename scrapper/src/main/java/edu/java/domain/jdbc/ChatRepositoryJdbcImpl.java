package edu.java.domain.jdbc;

import edu.java.domain.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ChatRepositoryJdbcImpl implements ChatRepository {

    private final JdbcClient jdbcClient;

    @Override
    public int add(Long chatId) {
        int rowsAffected = jdbcClient
            .sql("INSERT INTO chat(chat_id) VALUES (?)")
            .param(chatId)
            .update();

        log.info("Rows added: %s".formatted(rowsAffected));
        return rowsAffected;
    }

    @Override
    public int remove(Long chatId) {
        int rowsAffected = jdbcClient
            .sql("DELETE FROM chat WHERE chat_id = ?")
            .param(chatId)
            .update();

        log.info("Rows removed: %s".formatted(rowsAffected));
        return rowsAffected;
    }

    @Override
    public boolean isExists(Long chatId) {
        return jdbcClient
            .sql("SELECT * FROM chat WHERE chat_id = ?")
            .param(chatId)
            .query(Long.class)
            .optional().isPresent();
    }

    @Override
    public List<Long> findAll() {
        return jdbcClient
            .sql("SELECT * FROM chat")
            .query(
                (rs, rowNum) -> rs.getLong("chat_id")
            )
            .list();
    }
}
