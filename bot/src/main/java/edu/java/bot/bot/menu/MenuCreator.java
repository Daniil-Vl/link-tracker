package edu.java.bot.bot.menu;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.bot.Bot;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.CommandManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;

@Log4j2
@RequiredArgsConstructor
//@Component
public class MenuCreator implements CommandLineRunner {
    private final Bot bot;
    private final CommandManager commandManager;

    @Override
    public void run(String... args) {
        log.info("Try to send setMyCommands request to create 'menu' button in ui");
        SetMyCommands request = new SetMyCommands(
            commandManager
                .getAvailableCommands()
                .stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        );
        bot.execute(request);
        log.info("setMyCommands request was executed");
    }
}
