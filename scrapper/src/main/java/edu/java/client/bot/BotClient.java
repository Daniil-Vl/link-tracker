package edu.java.client.bot;

import edu.java.LinkUpdateRequest;

public interface BotClient {
    String sendLinkUpdateRequest(LinkUpdateRequest linkUpdateRequest);
}
