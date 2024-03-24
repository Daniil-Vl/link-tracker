package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.LinkUpdateRequest;
import edu.java.bot.telegram.message.MessageSender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateHandlerImpl implements LinkUpdateHandler {
    private final MessageSender messageSender;

    private SendResponse sendUpdateMessage(Long chatId, String url, String description) {
        return messageSender.sendMessage(new SendMessage(
            chatId,
            buildUpdateMessage(url, description)
        ));
    }

    @Override
    public void processLinkUpdates(List<LinkUpdateRequest> linkUpdateRequests) {
        for (LinkUpdateRequest linkUpdate : linkUpdateRequests) {
            for (Long userId : linkUpdate.ids()) {
                sendUpdateMessage(
                    userId,
                    linkUpdate.url(),
                    linkUpdate.description()
                );
            }
        }
    }

    private String buildUpdateMessage(String url, String description) {
        return "New update in %s:\n%s".formatted(url, description);
    }
}
