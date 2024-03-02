package edu.java.linkupdate;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    void update() {
        log.info("Running update method from LinkUpdaterScheduler");
    }
}
