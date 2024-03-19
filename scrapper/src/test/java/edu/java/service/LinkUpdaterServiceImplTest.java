package edu.java.service;

import edu.java.LinkUpdateRequest;
import edu.java.client.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.domain.LinkRepository;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.linkupdatesearching.SearchersManagerService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LinkUpdaterServiceImplTest {
    private final ApplicationConfig.Scheduler scheduler = new ApplicationConfig.Scheduler(
        true,
        Duration.ZERO,
        Duration.ZERO
    );
    @Mock
    private LinkService linkService;
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private BotClient botClient;
    @Mock
    private SearchersManagerService searchersManagerService;
    private LinkUpdaterServiceImpl linkUpdaterService;

    @BeforeEach
    void initService() {
        linkUpdaterService = new LinkUpdaterServiceImpl(
            linkService,
            scheduler,
            linkRepository,
            botClient,
            searchersManagerService
        );
    }

    @Test
    void testUpdate_noUpdatesFound() {
        Mockito.when(
            linkRepository.findAll(any(Duration.class))
        ).thenReturn(List.of());

        int processedUpdates = linkUpdaterService.update();

        assertThat(processedUpdates).isEqualTo(0);
        Mockito.verify(searchersManagerService, times(0))
            .getUpdates(any(LinkDto.class));
        Mockito.verify(botClient, times(0))
            .sendLinkUpdateRequest(any(LinkUpdateRequest.class));
    }

    @Test
    void testUpdate_updatesFound_thenSendLinkUpdateRequests() {
        List<LinkDto> links = List.of(
            new LinkDto(1L, URI.create("url"), OffsetDateTime.now(), OffsetDateTime.now())
        );
        List<LinkUpdate> updates = List.of(
            new LinkUpdate(1L, "url", "description")
        );
        LinkUpdate linkUpdate = updates.getFirst();
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            linkUpdate.id(),
            linkUpdate.url(),
            linkUpdate.description(),
            List.of()
        );

        Mockito.when(linkRepository.findAll(scheduler.forceCheckDelay())).thenReturn(links);
        Mockito.when(searchersManagerService.getUpdates(links.getFirst())).thenReturn(updates);
        Mockito.when(linkService.getAllSubscribers(links.getFirst().id())).thenReturn(List.of());

        int processedUpdates = linkUpdaterService.update();

        assertThat(processedUpdates).isEqualTo(1);
        Mockito.verify(botClient, times(1)).sendLinkUpdateRequest(linkUpdateRequest);
    }

}
