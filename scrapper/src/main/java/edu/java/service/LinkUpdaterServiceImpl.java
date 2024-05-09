package edu.java.service;

import edu.java.LinkUpdateRequest;
import edu.java.client.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.kafka.ScrapperQueueProducer;
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
    private final ApplicationConfig applicationConfig;
    private final LinkService linkService;
    private final SearchersManagerService searchersManagerService;
    private final BotClient botClient;
    private final ScrapperQueueProducer queueProducer;

    @Override
    @Transactional
    public int update() {
        log.info("Try to find updates...");

        // Find updates for links that have not been checked for a long time
        int numberOfProcessedUpdates = 0;
        List<LinkDto> linkDtoList = linkService.findAllOldLinks(applicationConfig.scheduler().forceCheckDelay());
        List<LinkUpdate> updates = new ArrayList<>();

        for (LinkDto linkDto : linkDtoList) {
            List<LinkUpdate> linkUpdates = searchersManagerService.getUpdates(linkDto);
            numberOfProcessedUpdates += linkUpdates.size();
            updates.addAll(linkUpdates);
        }

        // No updates were found
        if (updates.isEmpty()) {
            return 0;
        }

        // Send updates to the bot
        if (applicationConfig.useQueue()) {
            for (LinkUpdate linkUpdate : updates) {
                queueProducer.send(
                    linkUpdate,
                    linkService.getAllSubscribers(linkUpdate.linkId())
                );
            }
        } else {
            List<LinkUpdateRequest> linkUpdateRequests = updates.stream()
                .map(linkUpdate -> new LinkUpdateRequest(
                    linkUpdate.linkId(),
                    linkUpdate.url(),
                    linkUpdate.description(),
                    linkService.getAllSubscribers(linkUpdate.linkId())
                ))
                .toList();

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
