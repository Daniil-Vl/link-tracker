package edu.java.bot.controller;

import edu.java.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final LinkUpdateHandler linkUpdateHandler;

    @Override
    public String sendUpdate(List<LinkUpdateRequest> linkUpdateRequest) {
        linkUpdateHandler.processLinkUpdates(linkUpdateRequest);
        return "Updates processed";
    }
}
