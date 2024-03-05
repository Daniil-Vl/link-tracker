package edu.java.client.bot;

import edu.java.LinkUpdate;
import org.springframework.stereotype.Service;

@Service
public interface BotClient {
    String sendLinkUpdateRequest(LinkUpdate linkUpdate);
}
