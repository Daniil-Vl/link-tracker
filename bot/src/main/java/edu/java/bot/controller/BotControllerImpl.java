package edu.java.bot.controller;

import edu.java.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final LinkUpdateHandler linkUpdateHandler;

    @Override
    public String sendUpdate(@RequestBody List<LinkUpdateRequest> linkUpdateRequest) {
        linkUpdateHandler.processLinkUpdates(linkUpdateRequest);
        return "Updates processed";
    }
}
