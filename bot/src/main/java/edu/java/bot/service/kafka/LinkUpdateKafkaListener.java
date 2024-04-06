package edu.java.bot.service.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class LinkUpdateKafkaListener {
    private final LinkUpdateHandler linkUpdateHandler;

    @KafkaListener(topics = "${app.kafka.topic-name}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(LinkUpdateRequest linkUpdateRequest) {
        log.info("Received kafka message with payload = {}", linkUpdateRequest);
        linkUpdateHandler.processLinkUpdates(
            List.of(linkUpdateRequest)
        );
    }
}
