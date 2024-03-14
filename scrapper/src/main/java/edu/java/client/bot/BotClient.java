package edu.java.client.bot;

import edu.java.LinkUpdate;

public interface BotClient {
    String sendLinkUpdateRequest(LinkUpdate linkUpdate);
}
