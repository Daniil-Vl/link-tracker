package edu.java.service;

import edu.java.LinkUpdateRequest;
import edu.java.client.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.link_update_searching.SearchersManagerService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Log4j2
public class LinkUpdaterServiceImpl implements LinkUpdaterService {
    private final LinkService linkService;
    private final ApplicationConfig.Scheduler scheduler;
    private final BotClient botClient;
    private final SearchersManagerService searchersManagerService;

    @Override
    @Transactional
    public int update() {
        log.info("Try to find updates...");

        int numberOfProcessedUpdates = 0;
        List<LinkDto> linkDtoList = linkService.findAllOldLinks(scheduler.forceCheckDelay());
        List<LinkUpdateRequest> linkUpdateRequests = new ArrayList<>();

        for (LinkDto linkDto : linkDtoList) {
            List<LinkUpdate> updates = searchersManagerService.getUpdates(linkDto);
            numberOfProcessedUpdates += updates.size();

            for (LinkUpdate linkUpdate : updates) {
                linkUpdateRequests.add(
                    new LinkUpdateRequest(
                        linkUpdate.id(),
                        linkUpdate.url(),
                        linkUpdate.description(),
                        linkService.getAllSubscribers(linkDto.id())
                    ));
            }
        }

        if (!linkUpdateRequests.isEmpty()) {
            botClient.sendLinkUpdateRequest(linkUpdateRequests);
        }

        if (!linkDtoList.isEmpty()) {
            linkService.markNewCheck(
                linkDtoList.stream().map(LinkDto::id).toList(),
                OffsetDateTime.now()
            );
        }

        return numberOfProcessedUpdates;
    }
}
