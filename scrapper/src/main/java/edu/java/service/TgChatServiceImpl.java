package edu.java.service;

import edu.java.domain.ChatRepository;
import edu.java.exceptions.ChatNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;

@RequiredArgsConstructor
@Log4j2
public class TgChatServiceImpl implements TgChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(Long tgChatId) {
        try {
            chatRepository.add(tgChatId);
        } catch (DuplicateKeyException e) {
            log.warn("Tried to create already existing user with id = %s".formatted(tgChatId));
        }
    }

    @Override
    public void unregister(Long tgChatId) throws ChatNotExistException {
        int rowsAffected = chatRepository.remove(tgChatId);
        if (rowsAffected == 0) {
            throw new ChatNotExistException("Cannot remove non-existent chat");
        }
        log.info("User with id = %s has been unregistered".formatted(tgChatId));
    }

    @Override
    public boolean isAuthenticated(Long tgChatId) {
        return chatRepository.isExists(tgChatId);
    }
}
