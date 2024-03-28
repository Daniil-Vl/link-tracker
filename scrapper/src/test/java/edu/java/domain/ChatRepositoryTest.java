package edu.java.domain;

import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ChatRepositoryTest extends IntegrationTest {
    @Autowired
    protected JdbcClient jdbcClient;
    protected ChatRepository chatRepository;

    @BeforeEach
    abstract public void initRepository();

    @Test
    void addNewChat() {
        Long chatId = 1L;

        int rowsAffected = chatRepository.add(chatId);

        Long result = jdbcClient
            .sql("SELECT chat_id FROM chat WHERE chat_id = ?")
            .param(chatId)
            .query(Long.class)
            .single();

        assertThat(rowsAffected).isEqualTo(1);
        assertThat(result).isEqualTo(chatId);
    }

    @Test
    void remove() {
        Long chatId = 1L;
        chatRepository.add(chatId);

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
        chatRepository.add(chatId);

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
        for (Long chatId : chatIds) {
            chatRepository.add(chatId);
        }

        List<Long> actual = chatRepository.findAll();

        assertThat(actual).isEqualTo(chatIds);
    }
}
