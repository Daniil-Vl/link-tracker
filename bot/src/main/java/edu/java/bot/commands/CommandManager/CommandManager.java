package edu.java.bot.commands.CommandManager;

import edu.java.bot.commands.Command;
import java.util.List;

public interface CommandManager {
    /**
     * Return list of available commands
     */
    List<Command> getCommands();
}
