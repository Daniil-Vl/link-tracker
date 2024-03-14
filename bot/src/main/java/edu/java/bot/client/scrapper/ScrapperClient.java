package edu.java.bot.client.scrapper;

import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import java.net.URI;

public interface ScrapperClient {
    /**
     * Register chat in scrapper app
     *
     * @param id - chat id
     * @return string response
     */
    String registerChat(Integer id);

    /**
     * Removes chat with id
     *
     * @param id - chat id
     * @return string response
     */
    String deleteChat(Integer id);

    /**
     * Get all tracked links by user with certain chat id
     *
     * @param tgChatId - chat id
     * @return list of tracked links
     */
    ListLinksResponse getLinks(Integer tgChatId);

    /**
     * Start a tracking link by certain user
     *
     * @param tgChatId - user's chat id
     * @param link     - link to track
     * @return response with a tracked link
     */
    LinkResponse addLink(Integer tgChatId, URI link);

    /**
     * Stop a tracking link
     *
     * @param tgChatId          - user who now tracks the link
     * @param removeLinkRequest - request body with a link to remove
     * @return response with a removed link
     */
    LinkResponse deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest);
}
