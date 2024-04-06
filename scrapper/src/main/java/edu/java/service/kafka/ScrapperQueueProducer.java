package edu.java.service.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final ApplicationConfig applicationConfig;
    private final LinkService linkService;

    public void send(LinkUpdate linkUpdate) {
        LinkUpdateRequest request = new LinkUpdateRequest(
            linkUpdate.id(),
            linkUpdate.url(),
            linkUpdate.description(),
            linkService.getAllSubscribers(linkUpdate.id())
        );

        kafkaTemplate.send(
            applicationConfig.kafka().topicName(),
            request
        );
    }
}
