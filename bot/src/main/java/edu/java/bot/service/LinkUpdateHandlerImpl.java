package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.LinkUpdateRequest;
import edu.java.bot.telegram.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateHandlerImpl implements LinkUpdateHandler {
    private final MessageSender messageSender;

    @Override
    public SendResponse sendUpdateMessage(Long chatId, String url, String description) {
        return messageSender.sendMessage(new SendMessage(
            chatId,
            buildUpdateMessage(url, description)
        ));
    }

    @Override
    public void processLinkUpdate(LinkUpdateRequest linkUpdateRequest) {
        for (Long userId : linkUpdateRequest.ids()) {
            sendUpdateMessage(
                userId,
                linkUpdateRequest.url(),
                linkUpdateRequest.description()
            );
        }
    }

    private String buildUpdateMessage(String url, String description) {
        return "New update in %s:\n%s".formatted(url, description);
    }
}
