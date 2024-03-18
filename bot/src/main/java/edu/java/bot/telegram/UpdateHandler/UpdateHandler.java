package edu.java.bot.telegram.UpdateHandler;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public interface UpdateHandler {
    /**
     * If the user passed command, then bot will try to execute this
     * Otherwise reply with the predefined message of unsupported command
     *
     * @param update - update, that will be processed
     */
    void handleUpdate(Update update);
}
