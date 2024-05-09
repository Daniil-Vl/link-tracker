package edu.java.bot.metrics;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.UpdateHandler.UpdateHandlerImpl;
import edu.java.bot.telegram.message.MessageSender;
import edu.java.bot.telegram.processing.UserMessageProcessor;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessedMessagesMetricTest {

    long userId = 1;
    @Mock
    private UserMessageProcessor userMessageProcessor;
    @Mock
    private MessageSender messageSender;
    @Mock
    private Counter processedMessagesCounter;
    @InjectMocks
    private UpdateHandlerImpl updateHandler;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    /**
     * Set mocked update with correct update.message().chat().id() = userId
     */
    @BeforeEach
    void initMockedUpdate() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(userId);
    }

    @Test
    void givenUpdate_whenHandleUpdateAndProcessingSuccessfully_thenIncrementProcessedMessagesCounter() {
        SendMessage sendMessage = new SendMessage(1L, "text");
        when(userMessageProcessor.processUpdate(update))
            .thenReturn(sendMessage);

        updateHandler.handleUpdate(update);

        Mockito.verify(messageSender).sendMessage(sendMessage);
        Mockito.verify(processedMessagesCounter).increment();
    }

    @Test
    void givenUpdate_whenHandleUpdateAndProcessingFail_thenNotIncrementProcessedMessagesCounter() {
        when(userMessageProcessor.processUpdate(update))
            .thenThrow(RuntimeException.class);

        try {
            updateHandler.handleUpdate(update);
        } catch (Exception ignored) {
        }

        Mockito.verify(messageSender, never()).sendMessage(any());
        Mockito.verify(processedMessagesCounter, never()).increment();
    }

    @Test
    void givenUpdate_whenHandleUpdateAndResponseSendingFail_thenNotIncrementProcessedMessagesCounter() {
        SendMessage sendMessage = new SendMessage(1L, "text");
        when(userMessageProcessor.processUpdate(update))
            .thenReturn(sendMessage);
        when(messageSender.sendMessage(sendMessage))
            .thenThrow(RuntimeException.class);

        try {
            updateHandler.handleUpdate(update);
        } catch (Exception ignored) {
        }

        Mockito.verify(processedMessagesCounter, never()).increment();
    }
}
