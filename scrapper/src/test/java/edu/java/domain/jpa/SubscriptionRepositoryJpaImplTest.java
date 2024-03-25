package edu.java.domain.jpa;

import edu.java.domain.SubscriptionRepositoryTest;
import edu.java.domain.jpa.SubscriptionRepositoryJpaImpl;
import edu.java.domain.jpa.jpa_repositories.JpaChatRepository;
import edu.java.domain.jpa.jpa_repositories.JpaLinkRepository;
import edu.java.domain.jpa.jpa_repositories.JpaSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionRepositoryJpaImplTest extends SubscriptionRepositoryTest {
    @Autowired
    private JpaSubscriptionRepository jpaSubscriptionRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Override
    public void initRepository() {
        subscriptionRepository = new SubscriptionRepositoryJpaImpl(jpaSubscriptionRepository, jpaLinkRepository, jpaChatRepository);
    }
}
