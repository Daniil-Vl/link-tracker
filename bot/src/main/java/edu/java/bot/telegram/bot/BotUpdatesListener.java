package edu.java.bot.telegram.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.telegram.UpdateHandler.UpdateHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BotUpdatesListener implements UpdatesListener {

    private final UpdateHandler updateHandler;

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            this.updateHandler.handleUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
