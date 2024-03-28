package edu.java.service.link_update_searching;

import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import java.util.List;

public interface SearchersManagerService {
    /**
     * Get updates of an abstract link
     *
     * @param linkDto - link to get updates for
     * @return list of updates
     */
    List<LinkUpdate> getUpdates(LinkDto linkDto);
}
