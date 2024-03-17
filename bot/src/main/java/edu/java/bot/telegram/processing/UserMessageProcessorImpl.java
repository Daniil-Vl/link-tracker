package edu.java.bot.telegram.processing;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.commands.Command;
import edu.java.bot.telegram.commands.CommandManager.CommandManager;
import edu.java.bot.telegram.message.ReplyMessages;
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

            return command.handle(update);
        }

        log.info("Receive unknown command = %s".formatted(update.message().text().split(" ")[0]));
        return new SendMessage(
            update.message().chat().id(),
            ReplyMessages.UNSUPPORTED_COMMAND.getText()
        );
    }
}
