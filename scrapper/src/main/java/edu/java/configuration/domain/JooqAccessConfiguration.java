package edu.java.configuration.domain;

import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.SubscriptionRepository;
import edu.java.domain.jooq.ChatRepositoryJooqImpl;
import edu.java.domain.jooq.LinkRepositoryJooqImpl;
import edu.java.domain.jooq.SubscriptionRepositoryJooqImpl;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public ChatRepository chatRepository(DSLContext dslContext) {
        return new ChatRepositoryJooqImpl(dslContext);
    }

    @Bean
    public LinkRepository linkRepository(DSLContext dslContext) {
        return new LinkRepositoryJooqImpl(dslContext);
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(DSLContext dslContext) {
        return new SubscriptionRepositoryJooqImpl(dslContext);
    }
}
