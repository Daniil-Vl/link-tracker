package edu.java.client.bot;

import edu.java.LinkUpdateRequest;
import java.util.List;

public interface BotClient {
    String sendLinkUpdateRequest(List<LinkUpdateRequest> linkUpdateRequest);
}
