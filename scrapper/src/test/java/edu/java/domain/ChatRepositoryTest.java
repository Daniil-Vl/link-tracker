package edu.java.domain;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import static org.assertj.core.api.Assertions.assertThat;

abstract class ChatRepositoryTest extends IntegrationTest {
    @Autowired
    protected JdbcClient jdbcClient;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @MockBean
    protected GithubClient githubClient;
    @MockBean
    protected StackoverflowClient stackoverflowClient;
    protected ChatRepository chatRepository;

    @BeforeEach
    abstract void initRepository();

    @Test
    void addNewChat() {
        Long chatId = 1L;

        int rowsAffected = chatRepository.add(chatId);

        Long result = jdbcTemplate.queryForObject(
            "SELECT chat_id FROM chat WHERE chat_id = %s".formatted(chatId),
            Long.class
        );

        assertThat(rowsAffected).isEqualTo(1);
        assertThat(result).isEqualTo(chatId);
    }

    @Test
    void remove() {
        Long chatId = 1L;
        jdbcClient
            .sql("INSERT INTO chat(chat_id) VALUES (?)")
            .param(chatId)
            .update();

        int rowsAffected = chatRepository.remove(chatId);

        List<Long> chats = jdbcClient
            .sql("SELECT chat_id FROM chat")
            .query(Long.class)
            .list();

        assertThat(rowsAffected).isEqualTo(1);
        assertThat(chats).isEmpty();
    }

    @Test
    void isExists_onExistingUser_returnTrue() {
        Long chatId = 1L;
        jdbcClient
            .sql("INSERT INTO chat(chat_id) VALUES (?)")
            .param(chatId)
            .update();

        boolean result = chatRepository.isExists(chatId);

        assertThat(result).isTrue();
    }

    @Test
    void isExists_onNonExistingUser_returnFalse() {
        Long chatId = 1L;

        boolean result = chatRepository.isExists(chatId);

        assertThat(result).isFalse();
    }

    @Test
    void findAll() {
        List<Long> chatIds = List.of(1L, 2L);
        jdbcClient
            .sql("INSERT INTO chat(chat_id) VALUES (?), (?)")
            .param(chatIds.get(0))
            .param(chatIds.get(1))
            .update();

        List<Long> actual = chatRepository.findAll();

        assertThat(actual).isEqualTo(chatIds);
    }
}
