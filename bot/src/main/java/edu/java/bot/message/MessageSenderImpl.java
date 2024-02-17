package edu.java.bot.message;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.bot.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageSenderImpl implements MessageSender {

    private final Bot bot;

    @Override
    public SendResponse sendMessage(BotMessage botMessage) {
        return bot.sendMessage(new SendMessage(botMessage.userId(), botMessage.text()));
    }

    @Override
    public SendResponse sendMessage(SendMessage sendMessage) {
        return bot.sendMessage(sendMessage);
    }
}
