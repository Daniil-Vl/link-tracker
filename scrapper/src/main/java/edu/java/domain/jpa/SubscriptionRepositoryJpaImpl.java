package edu.java.domain.jpa;

import edu.java.domain.SubscriptionRepository;
import edu.java.domain.jpa.entities.LinkEntity;
import edu.java.domain.jpa.entities.SubscriptionEntity;
import edu.java.domain.jpa.entities.embeddable.SubscriptionKey;
import edu.java.domain.jpa.jpa_repositories.JpaChatRepository;
import edu.java.domain.jpa.jpa_repositories.JpaLinkRepository;
import edu.java.domain.jpa.jpa_repositories.JpaSubscriptionRepository;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.dao.SubscriptionDto;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryJpaImpl implements SubscriptionRepository {
    private final JpaSubscriptionRepository jpaSubscriptionRepository;
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;

    @Override
    public SubscriptionDto subscribe(Long chatId, Long linkId) {
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setSubscriptionKey(
            new SubscriptionKey(
                chatId,
                linkId
            )
        );
        subscription.setChat(jpaChatRepository.findById(chatId).get());
        subscription.setLink(jpaLinkRepository.findById(linkId).get());

        jpaSubscriptionRepository.saveAndFlush(subscription);

        return entityToDto(subscription);
    }

    @Override
    public Optional<SubscriptionDto> unsubscribe(Long chatId, Long linkId) {
        SubscriptionKey id = new SubscriptionKey(chatId, linkId);

        Optional<SubscriptionEntity> optionalSubscriptionEntity = jpaSubscriptionRepository.findById(id);
        if (optionalSubscriptionEntity.isEmpty()) {
            return Optional.empty();
        }

        SubscriptionEntity subscription = optionalSubscriptionEntity.get();
        jpaSubscriptionRepository.delete(subscription);

        return Optional.of(entityToDto(subscription));
    }

    @Override
    public List<Long> getAllSubscribers(Long linkId) {
        List<SubscriptionEntity> subscribers =
            jpaSubscriptionRepository.findAllBySubscriptionKeyLinkId(linkId);

        return subscribers
            .stream()
            .map(subscription -> subscription.getSubscriptionKey().getChatId())
            .toList();
    }

    @Override
    public List<LinkDto> getAllSubscriptions(Long chatId) {
        List<SubscriptionEntity> subscriptions = jpaSubscriptionRepository
            .findAllBySubscriptionKeyChatId(chatId);

        return subscriptions
            .stream()
            .map(subscription -> {
                LinkEntity link = subscription.getLink();
                return new LinkDto(
                    link.getId(),
                    URI.create(link.getUrl()),
                    link.getUpdatedAt(),
                    link.getLastCheckTime()
                );
            }).toList();
    }

    private SubscriptionDto entityToDto(SubscriptionEntity subscription) {
        return new SubscriptionDto(
            subscription.getSubscriptionKey().getChatId(),
            subscription.getSubscriptionKey().getLinkId()
        );
    }
}
