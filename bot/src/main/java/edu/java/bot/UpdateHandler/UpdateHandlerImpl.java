package edu.java.bot.UpdateHandler;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.CommandManager;
import edu.java.bot.commands.CommandManager.exceptions.UnknownCommandException;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.message.BotMessage;
import edu.java.bot.message.MessageSender;
import edu.java.bot.message.ReplyMessages;
import edu.java.bot.persistence.ResourceDB;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class UpdateHandlerImpl implements UpdateHandler {

    private final CommandManager commandManager;
    private final MessageSender messageSender;
    private final ResourceDB resourceDB;

    /**
     * If the user passed command, then bot will try to execute this
     * Otherwise reply with the predefined message of unsupported command
     *
     * @param update - update, that will be processed
     */
    @Override
    public void handleUpdate(Update update) {
        long userId = update.message().chat().id();
        log.info("Receive message from user with id - " + userId);

        try {
            Command command = commandManager.getCommandByName(update.message());
            log.info("Bot received command - " + command.command());

            // Send the reply message if an unauthenticated user tries to use commands
            if (!(command instanceof StartCommand) && !resourceDB.userExists(userId)) {
                messageSender.sendMessage(new BotMessage(userId, ReplyMessages.REQUIRE_SIGNING_IN.getText()));
                return;
            }

            messageSender.sendMessage(command.handle(update));
        } catch (UnknownCommandException e) {
            log.warn("Bot received unknown command");
            messageSender.sendMessage(new BotMessage(
                update.message().chat().id(),
                ReplyMessages.UNKNOWN_COMMAND.getText()
            ));
        }
    }
}
