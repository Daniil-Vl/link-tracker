package edu.java.domain.jpa.jpa_repositories;

import edu.java.domain.jpa.entities.SubscriptionEntity;
import edu.java.domain.jpa.entities.embeddable.SubscriptionKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, SubscriptionKey> {
    List<SubscriptionEntity> findAllBySubscriptionKeyLinkId(Long linkId);

    List<SubscriptionEntity> findAllBySubscriptionKeyChatId(Long chatId);
}
