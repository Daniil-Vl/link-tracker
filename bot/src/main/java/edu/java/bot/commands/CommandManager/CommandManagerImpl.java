package edu.java.bot.commands.CommandManager;

import edu.java.bot.commands.Command;
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
    public List<Command> getCommands() {
        return List.of(startCommand, helpCommand, listCommand, trackCommand, untrackCommand);
    }
}
