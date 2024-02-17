package edu.java.bot.UpdateHandler;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public interface UpdateHandler {
    void handleUpdate(Update update);
}
