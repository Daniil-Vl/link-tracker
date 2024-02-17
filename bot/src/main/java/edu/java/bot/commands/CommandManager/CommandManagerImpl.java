package edu.java.bot.commands.CommandManager;

import com.pengrad.telegrambot.model.Message;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.exceptions.UnknownCommandException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommandManagerImpl implements CommandManager {
    private final Command startCommand;
    private final Command helpCommand;
    private final Command listCommand;
    private final Command trackCommand;
    private final Command untrackCommand;

    @Override
    public List<Command> getAvailableCommands() {
        return List.of(startCommand, helpCommand, listCommand, trackCommand, untrackCommand);
    }

    @Override
    public Command getCommandByName(Message message) throws UnknownCommandException {
        return switch (message.text().split(" ")[0]) {
            case "/start" -> startCommand;
            case "/help" -> helpCommand;
            case "/list" -> listCommand;
            case "/track" -> trackCommand;
            case "/untrack" -> untrackCommand;
            default -> throw new UnknownCommandException();
        };
    }
}
