package edu.java.configuration.domain;

import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.SubscriptionRepository;
import edu.java.domain.jdbc.ChatRepositoryJdbcImpl;
import edu.java.domain.jdbc.LinkRepositoryJdbcImpl;
import edu.java.domain.jdbc.SubscriptionRepositoryJdbcImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public ChatRepository chatRepository(JdbcClient jdbcClient) {
        return new ChatRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public LinkRepository linkRepository(JdbcClient jdbcClient) {
        return new LinkRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(JdbcClient jdbcClient) {
        return new SubscriptionRepositoryJdbcImpl(jdbcClient);
    }
}
