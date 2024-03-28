package edu.java.bot.telegram.UpdateHandler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.message.MessageSender;
import edu.java.bot.telegram.processing.UserMessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class UpdateHandlerImpl implements UpdateHandler {

    private final UserMessageProcessor userMessageProcessor;
    private final MessageSender messageSender;

    @Override
    public void handleUpdate(Update update) {
        long userId = update.message().chat().id();
        log.info("Receive message from user with id = %s".formatted(userId));

        SendMessage response = userMessageProcessor.processUpdate(update);
        messageSender.sendMessage(response);
    }
}
