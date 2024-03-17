package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.LinkUpdateResponse;

public interface LinkUpdateHandler {
    SendResponse sendUpdateMessage(Long chatId, String url, String description);

    void processLinkUpdate(LinkUpdateResponse linkUpdateResponse);
}
