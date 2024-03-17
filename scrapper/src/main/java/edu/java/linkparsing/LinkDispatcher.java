package edu.java.linkparsing;

import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import java.util.List;

public interface LinkDispatcher {
    /**
     * Get updates of an abstract link
     *
     * @param linkDto - link to get updates for
     * @return list of updates
     */
    List<LinkUpdate> getUpdates(LinkDto linkDto);
}
