package edu.java.service.linkupdatesearching.searchers;

import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import java.net.URI;
import java.util.List;

/**
 * This entity can get updates for a certain link type (based on implementation of this interface)
 */
public interface LinkUpdateSearcher {
    String host();

    /**
     * Get updates for a certain link
     *
     * @param linkDto - link to get updates for
     * @return updates
     */
    List<LinkUpdate> getUpdates(LinkDto linkDto);

    /**
     * Checks if resource manager can process given url
     *
     * @param url - url to process
     * @return true, if can
     */
    default boolean canProcess(URI url) {
        return host().equals(url.getHost());
    }
}
