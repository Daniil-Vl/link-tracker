package edu.java.service.jdbc;

import edu.java.LinkUpdateResponse;
import edu.java.client.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.domain.jdbc.LinkRepositoryJdbcImpl;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdaterService;
import edu.java.service.linkupdatesearching.SearchersManagerService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class LinkUpdaterServiceJdbcImpl implements LinkUpdaterService {
    private final LinkService linkService;
    private final ApplicationConfig.Scheduler scheduler;
    private final LinkRepositoryJdbcImpl linkRepositoryJdbc;
    private final BotClient botClient;
    private final SearchersManagerService searchersManagerService;

    @Override
    public int update() {
        log.info("Call update method inside LinkUpdaterJdbcImpl");
        List<LinkDto> linkDtoList = linkRepositoryJdbc.findAll(scheduler.forceCheckDelay());
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
                    new LinkUpdateResponse(
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
