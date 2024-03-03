package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.commands.CommandManager.CommandManager;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class HelpCommand implements Command {

    private final CommandManager commandManager;

    public HelpCommand(@Lazy CommandManager commandManager) {
        this.commandManager = commandManager;
    }

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

        List<Command> commands = commandManager.getCommands();

        for (int ind = 0; ind < commands.size(); ind++) {
            Command command = commands.get(ind);
            stringBuilder.append("%d) %s - %s\n".formatted(ind + 1, command.command(), command.description()));
        }

        return stringBuilder.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();
        return new SendMessage(userId, helpMessage());
    }
}
