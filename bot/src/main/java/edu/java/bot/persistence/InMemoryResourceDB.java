package edu.java.bot.persistence;

import edu.java.bot.links.Link;
import edu.java.bot.persistence.exceptions.UserNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Temporary in-memory database
 */
@Component
@Log4j2
public class InMemoryResourceDB implements ResourceDB {
    private final HashMap<Long, List<Link>> resources = new HashMap<>();

    @Override
    public void addUser(long userId) {
        resources.putIfAbsent(userId, new ArrayList<>());
    }

    @Override
    public boolean userExists(long userId) {
        return resources.containsKey(userId);
    }

    @Override
    public boolean track(long userId, Link link) throws UserNotFoundException {
        List<Link> userResources = resources.get(userId);

        if (userResources == null) {
            throw new UserNotFoundException("Cannot add resources to non-existent user with id = %s".formatted(userId));
        }

        if (userResources.contains(link)) {
            return true;
        }

        userResources.add(link);
        log.info("URI - \"%s\" resource was added to user(id=%s)".formatted(link.url().toString(), userId));
        return false;
    }

    @Override
    public boolean untrack(long userId, Link link) throws UserNotFoundException {
        List<Link> userResources = resources.get(userId);

        if (userResources == null) {
            log.error("Tried to remove resource from non-existent user with id = %s".formatted(userId));
            throw new UserNotFoundException("Cannot remove resource from non-existent user with id = %s".formatted(
                userId));
        }

        boolean contains = userResources.remove(link);

        if (contains) {
            log.info("Remove link = %s from user with id = %s".formatted(link, userId));
        } else {
            log.info("Try to remove link = %s, which wasn't tracked".formatted(link));
        }
        return contains;
    }

    @Override
    public @NotNull List<Link> getTrackedResources(long userId) throws UserNotFoundException {
        if (!userExists(userId)) {
            throw new UserNotFoundException(
                "Cannot get list of tracked resources of non-existent user with id = %s".formatted(
                    userId));
        }
        return resources.get(userId);
    }
}
