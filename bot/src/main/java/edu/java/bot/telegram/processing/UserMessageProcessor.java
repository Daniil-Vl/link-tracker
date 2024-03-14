package edu.java.bot.telegram.processing;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface UserMessageProcessor {
    /**
     * Process update using available commands
     * If the message does not contain any of the supported commands,
     * then return the default message about unknown command
     */
    SendMessage processUpdate(Update update);
}
