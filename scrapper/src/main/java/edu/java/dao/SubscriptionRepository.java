package edu.java.dao;

import edu.java.dto.dao.LinkDto;
import edu.java.dto.dao.SubscriptionDto;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    /**
     * Creates subscription for certain resource
     *
     * @param chatId - user to subscribe
     * @param linkId - resource for subscription
     * @return chat id and link id
     */
    SubscriptionDto subscribe(Long chatId, Long linkId);

    /**
     * Stop subscription for certain resource
     *
     * @param chatId - user's id
     * @param linkId - resource's id
     * @return chat id and link id
     */
    Optional<SubscriptionDto> unsubscribe(Long chatId, Long linkId);

    /**
     * Return all subscribed users to a certain resource
     *
     * @param linkId - resource id
     * @return list of users
     */
    List<Long> getAllSubscribedUsers(Long linkId);

    /**
     * Return all resources that the user is tracking
     *
     * @param chatId - user's telegram chat id
     * @return list of links
     */
    List<LinkDto> getAllSubscriptions(Long chatId);
}
