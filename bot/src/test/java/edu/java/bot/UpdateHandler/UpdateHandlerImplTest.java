package edu.java.bot.UpdateHandler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.CommandManager;
import edu.java.bot.commands.CommandManager.CommandManagerImpl;
import edu.java.bot.message.BotMessage;
import edu.java.bot.message.MessageSender;
import edu.java.bot.message.ReplyMessages;
import edu.java.bot.persistence.ResourceDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UpdateHandlerImplTest {
    long userId = 1;
    @Mock
    private MessageSender messageSender;
    @Mock
    private ResourceDB resourceDB;
    private CommandManager commandManager;
    private UpdateHandler updateHandler;
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

    /**
     * Set mocked update with correct update.message().chat().id() = userId
     */
    void initMockedUpdate() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(userId);
    }

    /**
     * Set up mocked command's handle method
     */
    void initCommands() {
        Mockito.when(startCommand.handle(update))
            .thenReturn(new SendMessage(userId, "/start handle"));

        Mockito.when(helpCommand.handle(update))
            .thenReturn(new SendMessage(userId, "/help handle"));

        Mockito.when(listCommand.handle(update))
            .thenReturn(new SendMessage(userId, "/list handle"));

        Mockito.when(trackCommand.handle(update))
            .thenReturn(new SendMessage(userId, "/track handle"));

        Mockito.when(untrackCommand.handle(update))
            .thenReturn(new SendMessage(userId, "/untrack handle"));
    }

    void initCommandManager() {
        commandManager = new CommandManagerImpl(startCommand, helpCommand, listCommand, trackCommand, untrackCommand);
    }

    void initUpdateHandler() {
        updateHandler = new UpdateHandlerImpl(commandManager, messageSender, resourceDB);
    }

    @BeforeEach
    void initAll() {
        initMockedUpdate();
        initCommands();
        initCommandManager();
        initUpdateHandler();
    }

    void setMessageText(String text) {
        Mockito.when(message.text()).thenReturn(text);
    }

    void addUserToResourceDB(long id) {
        Mockito.when(resourceDB.userExists(id)).thenReturn(true);
    }

    /**
     * Trying any command for auth user
     */
    @Test
    void givenAuthUser_whenHandleUpdate_thenSendCommandHandleMessage() {
        addUserToResourceDB(userId);
        setMessageText("/help");
        updateHandler.handleUpdate(update);
        Mockito.verify(messageSender).sendMessage(helpCommand.handle(update));
    }

    /**
     * Unauthenticated user tries to run command
     */
    @Test
    void givenUnauthenticatedUser_whenHandleCommandUpdate_thenReturnRequireSigningInMessage() {
        setMessageText("/help");
        updateHandler.handleUpdate(update);
        Mockito.verify(messageSender).sendMessage(new BotMessage(userId, ReplyMessages.REQUIRE_SIGNING_IN.getText()));
    }

    /**
     * Trying unknown command
     */
    @Test
    void givenUnknownCommand_whenHandleCommandUpdate_thenReturnUnknownCommandMessage() {
        setMessageText("/unsupported_command");
        updateHandler.handleUpdate(update);
        Mockito.verify(messageSender).sendMessage(new BotMessage(userId, ReplyMessages.UNKNOWN_COMMAND.getText()));
    }
}
