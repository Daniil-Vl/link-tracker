package edu.java.service.jdbc;

import edu.java.dao.jdbc.ChatRepositoryJdbcImpl;
import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class TgChatServiceJdbcImpl implements TgChatService {
    private final ChatRepositoryJdbcImpl chatRepositoryJdbc;

    @Override
    public void register(Long tgChatId) {
        try {
            chatRepositoryJdbc.add(tgChatId);
        } catch (DuplicateKeyException e) {
            log.warn("Tried to create already existing user with id = %s".formatted(tgChatId));
        }
    }

    @Override
    public void unregister(Long tgChatId) {
        chatRepositoryJdbc.remove(tgChatId);
        log.info("User with id = %s has been unregistered".formatted(tgChatId));
    }

    @Override
    public boolean isAuthenticated(Long tgChatId) {
        return chatRepositoryJdbc.isExists(tgChatId);
    }
}
