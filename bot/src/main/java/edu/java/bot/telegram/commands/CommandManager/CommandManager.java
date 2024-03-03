package edu.java.bot.telegram.commands.CommandManager;

import edu.java.bot.telegram.commands.Command;
import java.util.List;

public interface CommandManager {
    /**
     * Return list of available commands
     */
    List<Command> getCommands();
}
