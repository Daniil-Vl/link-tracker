package edu.java.service;

import edu.java.domain.ChatRepository;
import edu.java.exceptions.ChatNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TgChatServiceImplTest {
    @Mock
    private ChatRepository chatRepository;
    @InjectMocks
    private TgChatServiceImpl tgChatService;

    @Test
    void registerCallingRepositoryRegisterMethod() {
        Long tgChatId = 1L;
        tgChatService.register(tgChatId);

        Mockito.verify(chatRepository).add(tgChatId);
    }

    @Test
    void unregisterExistingIdWorksWithoutExceptions() throws ChatNotExistException {
        Long tgChatId = 1L;
        Mockito.when(chatRepository.remove(tgChatId)).thenReturn(1);

        tgChatService.unregister(tgChatId);
    }

    @Test
    void unregisterNonExistentIdThrowsException() {
        Long tgChatId = 1L;
        Mockito.when(chatRepository.remove(tgChatId)).thenReturn(0);

        assertThatThrownBy(() -> {
            tgChatService.unregister(tgChatId);
        }).isInstanceOf(ChatNotExistException.class);
    }

    @Test
    void isAuthenticatedPositiveCase() {
        Long tgChatId = 1L;
        Mockito.when(chatRepository.isExists(tgChatId)).thenReturn(true);
        assertThat(tgChatService.isAuthenticated(tgChatId)).isTrue();
    }

    @Test
    void isAuthenticatedNegativeCase() {
        Long tgChatId = 1L;
        Mockito.when(chatRepository.isExists(tgChatId)).thenReturn(false);
        assertThat(tgChatService.isAuthenticated(tgChatId)).isFalse();
    }
}
