package edu.java.scheduling;

import edu.java.service.LinkUpdaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    void update() {
        log.info("Running update method from LinkUpdaterScheduler");
        int updatesProcessed = linkUpdaterService.update();
        log.info("Processed %s updates".formatted(updatesProcessed));
    }
}
