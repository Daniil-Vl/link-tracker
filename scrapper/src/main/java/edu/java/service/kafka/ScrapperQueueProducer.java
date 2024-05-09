package edu.java.service.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final ApplicationConfig applicationConfig;

    public void send(LinkUpdate linkUpdate, List<Long> subscribers) {
        LinkUpdateRequest request = new LinkUpdateRequest(
            linkUpdate.linkId(),
            linkUpdate.url(),
            linkUpdate.description(),
            subscribers
        );

        kafkaTemplate.send(
            applicationConfig.kafka().topicName(),
            request
        );
    }
}
