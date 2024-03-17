package edu.java.client.bot;

import edu.java.LinkUpdateResponse;

public interface BotClient {
    String sendLinkUpdateRequest(LinkUpdateResponse linkUpdateResponse);
}
