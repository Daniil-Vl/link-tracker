package edu.java.service;

import edu.java.LinkUpdateRequest;
import edu.java.client.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.domain.LinkRepository;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.linkupdatesearching.SearchersManagerService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class LinkUpdaterServiceImpl implements LinkUpdaterService {
    private final LinkService linkService;
    private final ApplicationConfig.Scheduler scheduler;
    private final LinkRepository linkRepository;
    private final BotClient botClient;
    private final SearchersManagerService searchersManagerService;

    @Override
    public int update() {
        log.info("Call update method inside LinkUpdaterImpl");
        List<LinkDto> linkDtoList = linkRepository.findAll(scheduler.forceCheckDelay());
        int numberOfProcessedUpdates = 0;

        for (LinkDto linkDto : linkDtoList) {
            linkService.markNewCheck(linkDto.id(), OffsetDateTime.now());
            List<LinkUpdate> updates = searchersManagerService.getUpdates(linkDto);

            if (updates.isEmpty()) {
                continue;
            }

            for (LinkUpdate update : updates) {
                numberOfProcessedUpdates += 1;
                botClient.sendLinkUpdateRequest(
                    new LinkUpdateRequest(
                        update.id(),
                        update.url(),
                        update.description(),
                        linkService.getAllSubscribers(linkDto.id())
                    )
                );
            }
        }

        return numberOfProcessedUpdates;
    }
}
