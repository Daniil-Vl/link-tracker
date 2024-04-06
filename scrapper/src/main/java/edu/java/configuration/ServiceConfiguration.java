package edu.java.configuration;

import edu.java.client.bot.BotClient;
import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.SubscriptionRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkServiceImpl;
import edu.java.service.LinkUpdaterService;
import edu.java.service.LinkUpdaterServiceImpl;
import edu.java.service.TgChatService;
import edu.java.service.TgChatServiceImpl;
import edu.java.service.kafka.ScrapperQueueProducer;
import edu.java.service.link_update_searching.SearchersManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceConfiguration {
    private final ChatRepository chatRepositoryJdbcImpl;
    private final LinkRepository linkRepositoryJdbcImpl;
    private final SubscriptionRepository subscriptionRepositoryJdbcImpl;

    @Bean
    public TgChatService tgChatService() {
        return new TgChatServiceImpl(
            chatRepositoryJdbcImpl
        );
    }

    @Bean
    public LinkService linkService(TgChatService tgChatService) {
        return new LinkServiceImpl(
            linkRepositoryJdbcImpl,
            subscriptionRepositoryJdbcImpl,
            tgChatService
        );
    }

    @Bean
    public LinkUpdaterService linkUpdaterService(
        LinkService linkService,
        BotClient botClient,
        SearchersManagerService searchersManagerService,
        ApplicationConfig applicationConfig,
        ScrapperQueueProducer queueProducer
    ) {
        return new LinkUpdaterServiceImpl(
            applicationConfig,
            linkService,
            searchersManagerService,
            botClient,
            queueProducer
        );
    }
}
