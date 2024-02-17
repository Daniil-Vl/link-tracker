package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Bot implements AutoCloseable {
    private final TelegramBot telegramBot;

    public Bot(TelegramBot telegramBot, @Lazy UpdatesListener updatesListener) {
        this.telegramBot = telegramBot;
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
}
