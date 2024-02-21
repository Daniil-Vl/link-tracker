package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager.CommandManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Bot implements AutoCloseable {
    private final TelegramBot telegramBot;
    private final CommandManager commandManager;

    public Bot(TelegramBot telegramBot, @Lazy UpdatesListener updatesListener, CommandManager commandManager) {
        this.telegramBot = telegramBot;
        this.commandManager = commandManager;
        this.telegramBot.setUpdatesListener(updatesListener);
        log.info("Bot started...");
    }

    public SendResponse sendMessage(SendMessage sendMessage) {
        return this.telegramBot.execute(sendMessage);
    }

    @Override
    public void close() {
        log.info("Bot stops...");
    }

    /**
     * Execute request to telegram api
     *
     * @param request - request to execute
     */
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @PostConstruct
    private void createCommandMenu() {
        log.info("Try to send setMyCommands request to create 'menu' button in ui");
        SetMyCommands request = new SetMyCommands(
            commandManager
                .getAvailableCommands()
                .stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        );
        execute(request);
        log.info("setMyCommands request was executed");
    }
}
