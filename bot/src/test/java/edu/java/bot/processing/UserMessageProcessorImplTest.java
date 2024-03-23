package edu.java.bot.processing;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.commands.Command;
import edu.java.bot.telegram.commands.CommandManager.CommandManagerImpl;
import edu.java.bot.telegram.message.ReplyMessages;
import edu.java.bot.telegram.processing.UserMessageProcessor;
import edu.java.bot.telegram.processing.UserMessageProcessorImpl;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMessageProcessorImplTest {
    long userId = 1;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Command startCommand;
    @Mock
    private Command helpCommand;
    @Mock
    private Command listCommand;
    @Mock
    private Command trackCommand;
    @Mock
    private Command untrackCommand;
    private UserMessageProcessor userMessageProcessor;

    /**
     * Set mocked update with correct update.message().chat().id() = userId
     */
    void initMockedUpdate() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(userId);
    }

    void initUserMessageProcessor() {
        userMessageProcessor = new UserMessageProcessorImpl(
            new CommandManagerImpl(startCommand, helpCommand, listCommand, trackCommand, untrackCommand)
        );
    }

    @BeforeEach
    void initAll() {
        initMockedUpdate();
        initUserMessageProcessor();
    }

    void setMessageText(String text) {
        Mockito.when(message.text()).thenReturn(text);
    }

    /**
     * Check that command manager sends UNKNOWN_COMMAND message, when encounters unsupported commands
     */
    @Test
    void givenUnsupportedCommand_whenProcessUpdate_thenReturnUnknownCommandMessage() {
        setMessageText("/asd");

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            ReplyMessages.UNSUPPORTED_COMMAND.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = userMessageProcessor.processUpdate(update);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

}
