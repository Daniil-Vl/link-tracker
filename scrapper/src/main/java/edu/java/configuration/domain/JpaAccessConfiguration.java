package edu.java.configuration.domain;

import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.SubscriptionRepository;
import edu.java.domain.jpa.ChatRepositoryJpaImpl;
import edu.java.domain.jpa.LinkRepositoryJpaImpl;
import edu.java.domain.jpa.SubscriptionRepositoryJpaImpl;
import edu.java.domain.jpa.jpa_repositories.JpaChatRepository;
import edu.java.domain.jpa.jpa_repositories.JpaLinkRepository;
import edu.java.domain.jpa.jpa_repositories.JpaSubscriptionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public ChatRepository chatRepository(JpaChatRepository jpaChatRepository) {
        return new ChatRepositoryJpaImpl(jpaChatRepository);
    }

    @Bean
    public LinkRepository linkRepository(JpaLinkRepository jpaLinkRepository) {
        return new LinkRepositoryJpaImpl(jpaLinkRepository);
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository,
        JpaSubscriptionRepository jpaSubscriptionRepository
    ) {
        return new SubscriptionRepositoryJpaImpl(
            jpaSubscriptionRepository,
            jpaLinkRepository,
            jpaChatRepository
        );
    }
}
