package edu.java.bot.commands.CommandManager;

import com.pengrad.telegrambot.model.Message;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.exceptions.UnknownCommandException;
import java.util.List;

public interface CommandManager {
    /**
     * Return list of available commands
     */
    List<Command> getAvailableCommands();

    /**
     * Try to parse command from the message
     *
     * @param message - message, to parse command from
     * @return Command, if the message contains supported command, otherwise returns null
     */
    Command getCommandByName(Message message) throws UnknownCommandException;
}
