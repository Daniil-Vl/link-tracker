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
import edu.java.service.linkupdatesearching.SearchersManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceConfiguration {
    private final ChatRepository chatRepositoryJdbcImpl;
    private final LinkRepository linkRepositoryJdbcImpl;
    private final SubscriptionRepository subscriptionRepositoryJdbcImpl;

    private final ChatRepository chatRepositoryJooqImpl;
    private final LinkRepository linkRepositoryJooqImpl;
    private final SubscriptionRepository subscriptionRepositoryJooqImpl;

    private final ApplicationConfig.Scheduler scheduler;
    private final BotClient botClient;

    @Bean
    public TgChatService tgChatService() {
        return new TgChatServiceImpl(
//            chatRepositoryJdbcImpl
            chatRepositoryJooqImpl
        );
    }

    @Bean
    public LinkService linkService(TgChatService tgChatService) {
        return new LinkServiceImpl(
//            linkRepositoryJdbcImpl,
            linkRepositoryJooqImpl,
//            subscriptionRepositoryJdbcImpl,
            subscriptionRepositoryJooqImpl,
            tgChatService
        );
    }

    @Bean
    public LinkUpdaterService linkUpdaterService(
        LinkService linkService,
        SearchersManagerService searchersManagerService
    ) {
        return new LinkUpdaterServiceImpl(
            linkService,
            scheduler,
//            linkRepositoryJdbcImpl,
            linkRepositoryJooqImpl,
            botClient,
            searchersManagerService
        );
    }
}
