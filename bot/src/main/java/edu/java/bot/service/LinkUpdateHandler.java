package edu.java.bot.service;

import edu.java.LinkUpdateRequest;
import java.util.List;

public interface LinkUpdateHandler {
    /**
     * Send messages about updates to the subscribed users
     *
     * @param linkUpdateRequest - list of links updates
     */
    void processLinkUpdates(List<LinkUpdateRequest> linkUpdateRequest);
}
