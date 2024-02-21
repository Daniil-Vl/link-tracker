package edu.java.bot.processing;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.CommandManager;
import edu.java.bot.message.ReplyMessages;
import edu.java.bot.persistence.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Log4j2
public class UserMessageProcessorImpl implements UserMessageProcessor {
    private final CommandManager commandManager;

    @Override
    public SendMessage processUpdate(Update update) {
        for (Command command : commandManager.getCommands()) {
            if (!command.supports(update)) {
                continue;
            }

            try {
                return command.handle(update);
            } catch (UserNotFoundException e) {
                log.warn(
                    "Tried to execute command %s with non-existent user with id = %s"
                        .formatted(command.command(), update.message().chat().id())
                );
                return new SendMessage(
                    update.message().chat().id(),
                    ReplyMessages.REQUIRE_SIGNING_IN.getText()
                );
            }
        }

        log.info("Receive unknown command = %s".formatted(update.message().text().split(" ")[0]));
        return new SendMessage(
            update.message().chat().id(),
            ReplyMessages.UNSUPPORTED_COMMAND.getText()
        );
    }
}
