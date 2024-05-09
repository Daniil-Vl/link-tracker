package edu.java.bot.service;

import edu.java.LinkUpdateRequest;
import java.util.List;

public interface LinkUpdateHandler {
    /**
     * Send messages about updates to the subscribed users
     * @param linkUpdateRequestList - list of link updates
     */
    void processLinkUpdates(List<LinkUpdateRequest> linkUpdateRequestList);

    /**
     * Send messages about update to the subscribed users
     * @param linkUpdateRequest - link update
     */
    void processLinkUpdate(LinkUpdateRequest linkUpdateRequest);
}
