package edu.java.bot.telegram.persistence;

import edu.java.bot.telegram.links.Link;
import edu.java.bot.telegram.persistence.exceptions.UserNotFoundException;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Temporary database, using to store user's tracked resources
 */
@Component
public interface ResourceDB {
    void addUser(long userId);

    boolean userExists(long userId);

    /**
     * Make record about user's new tracked resource
     *
     * @param userId - user, which will be tracking new resource
     * @param link   - resources uri
     * @return true, if db already contains resource and it has not been added, otherwise false
     */
    boolean track(long userId, Link link) throws UserNotFoundException;

    /**
     * Remove track record
     * Returns true, if db contained link
     *
     * @param userId - user id
     * @param link   - resource link
     * @return true, if db contains resource, otherwise false
     */
    boolean untrack(long userId, Link link) throws UserNotFoundException;

    List<Link> getTrackedResources(long userId) throws UserNotFoundException;
}
