package edu.java.bot.telegram.message;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public interface MessageSender {
    SendResponse sendMessage(BotMessage botMessage);

    SendResponse sendMessage(SendMessage sendMessage);
}
