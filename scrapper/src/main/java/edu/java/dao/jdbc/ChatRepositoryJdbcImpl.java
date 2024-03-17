package edu.java.dao.jdbc;

import edu.java.dao.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ChatRepositoryJdbcImpl implements ChatRepository {
    private static final String ADD_CHAT_QUERY = "INSERT INTO chat(chat_id) VALUES (?)";
    private static final String REMOVE_CHAT_QUERY = "DELETE FROM chat WHERE chat_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM chat";
    private static final String FIND_BY_ID = "SELECT * FROM chat WHERE chat_id = ?";

    private final JdbcClient jdbcClient;

    @Override
    public int add(Long chatId) {
        int rowsAffected = jdbcClient
            .sql(ADD_CHAT_QUERY)
            .param(chatId)
            .update();

        log.info("Rows added: %s".formatted(rowsAffected));
        return rowsAffected;
    }

    @Override
    public int remove(Long chatId) {
        int rowsAffected = jdbcClient
            .sql(REMOVE_CHAT_QUERY)
            .param(chatId)
            .update();

        log.info("Rows removed: %s".formatted(rowsAffected));
        return rowsAffected;
    }

    @Override
    public boolean isExists(Long chatId) {
        return jdbcClient
            .sql(FIND_BY_ID)
            .param(chatId)
            .query(Long.class)
            .optional().isPresent();
    }

    @Override
    public List<Long> findAll() {
        return jdbcClient
            .sql(FIND_ALL_QUERY)
            .query(
                (rs, rowNum) -> rs.getLong("chat_id")
            )
            .list();
    }
}
