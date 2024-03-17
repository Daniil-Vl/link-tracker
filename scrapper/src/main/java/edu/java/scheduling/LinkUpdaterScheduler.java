package edu.java.scheduling;

import edu.java.service.LinkUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    void update() {
        log.info("Running update method from LinkUpdaterScheduler");
        int updatesProcessed = linkUpdater.update();
        log.info("Processed %s updates".formatted(updatesProcessed));
    }
}
