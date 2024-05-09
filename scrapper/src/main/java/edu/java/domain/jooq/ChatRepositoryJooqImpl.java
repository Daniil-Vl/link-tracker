package edu.java.domain.jooq;

import edu.java.domain.ChatRepository;
import edu.java.domain.jooq.generated.tables.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ChatRepositoryJooqImpl implements ChatRepository {
    private final DSLContext dslContext;
    Chat chat = Chat.CHAT;

    @Override
    public int add(Long chatId) {
        int rowsAffected = dslContext
                .insertInto(chat)
                .set(chat.CHAT_ID, chatId)
                .execute();

        log.info("Jooq repository add query rows affected: %s".formatted(rowsAffected));

        return rowsAffected;
    }

    @Override
    public int remove(Long chatId) {
        int rowsAffected = dslContext
                .delete(chat)
                .where(chat.CHAT_ID.eq(chatId))
                .execute();

        log.info("Jooq repository remove query rows affected: %s".formatted(rowsAffected));

        return rowsAffected;
    }

    @Override
    public boolean isExists(Long chatId) {
        List<Long> fetched = dslContext
                .select(chat.CHAT_ID)
                .from(chat)
                .where(chat.CHAT_ID.eq(chatId))
                .fetch(chat.CHAT_ID);

        log.info("Jooq repository isExists query result:");
        log.info(fetched);

        return !fetched.isEmpty();
    }

    @Override
    public List<Long> findAll() {
        List<Long> fetched = dslContext
                .select(chat.CHAT_ID)
                .from(chat)
                .fetch(chat.CHAT_ID);

        log.info("Jooq repository findAll query result:");
        log.info(fetched);

        return fetched;
    }
}
