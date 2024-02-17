package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.persistence.ResourceDB;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class HelpCommand implements Command {
    private final ResourceDB resourceDB;

    private List<Command> availableCommands = List.of(
        new StartCommand(null),
        new ListCommand(null),
        new TrackCommand(null, null),
        new UntrackCommand(null, null)
    );

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "show info about all commands";
    }

    @Override
    public String helpMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("These bot is designed to help you track links from various internet resources\n\n");
        stringBuilder.append("You can control me by sending these commands: \n");

        for (int ind = 0; ind < availableCommands.size(); ind++) {
            Command command = availableCommands.get(ind);
            stringBuilder.append("%d) %s - %s\n".formatted(ind + 1, command.command(), command.description()));
        }
        stringBuilder.append("%d) %s - %s".formatted(availableCommands.size() + 1, command(), description()));

        return stringBuilder.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();
        return new SendMessage(userId, helpMessage());
    }
}
