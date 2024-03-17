package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.LinkUpdateResponse;

public interface LinkUpdateHandler {
    public SendResponse sendUpdateMessage(Long chatId, String url, String description);

    public void processLinkUpdate(LinkUpdateResponse linkUpdateResponse);
}
