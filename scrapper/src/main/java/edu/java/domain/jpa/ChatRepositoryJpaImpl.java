package edu.java.domain.jpa;

import edu.java.domain.ChatRepository;
import edu.java.domain.jpa.entities.ChatEntity;
import edu.java.domain.jpa.jpa_repositories.JpaChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRepositoryJpaImpl implements ChatRepository {
    private final JpaChatRepository jpaChatRepository;

    @Override
    public int add(Long chatId) {
        ChatEntity chat = new ChatEntity();
        chat.setChatId(chatId);
        jpaChatRepository.saveAndFlush(chat);
        return 1;
    }

    @Override
    public int remove(Long chatId) {
        jpaChatRepository.deleteById(chatId);
        return 1;
    }

    @Override
    public boolean isExists(Long chatId) {
        return jpaChatRepository.existsById(chatId);
    }

    @Override
    public List<Long> findAll() {
        return jpaChatRepository
            .findAll()
            .stream()
            .map(ChatEntity::getChatId)
            .toList();
    }
}
