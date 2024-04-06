package edu.java.bot.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.LinkUpdateHandler;
import edu.java.bot.telegram.bot.Bot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class LinkUpdateKafkaListenerTest extends IntegrationTest {

    @Autowired
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @Autowired
    private ApplicationConfig applicationConfig;

    @MockBean
    private LinkUpdateHandler linkUpdateHandler;

    @MockBean
    private Bot bot;

    void sendRequest(LinkUpdateRequest linkUpdateRequest)
        throws ExecutionException, InterruptedException, TimeoutException {
        kafkaTemplate.send(
            applicationConfig.kafka().topicName(),
            linkUpdateRequest
        ).get(10L, TimeUnit.SECONDS);
    }

    @Test
    void testMessageReceiving() throws ExecutionException, InterruptedException, TimeoutException {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            "url",
            "description",
            List.of()
        );
        sendRequest(linkUpdateRequest);

        verify(linkUpdateHandler, timeout(10000).atLeastOnce()).processLinkUpdates(List.of(linkUpdateRequest));
    }

    @TestConfiguration
    @RequiredArgsConstructor
    public static class KafkaTestConfig {
        private final ApplicationConfig applicationConfig;

        @Bean
        public ProducerFactory<String, LinkUpdateRequest> producerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KAFKA.getBootstrapServers()
            );
            props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
            );
            props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
            );
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate(
            ProducerFactory<String, LinkUpdateRequest> producerFactory
        ) {
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KAFKA.getBootstrapServers()
            );
            props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                applicationConfig.kafka().groupId()
            );
            props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
            );
            return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(LinkUpdateRequest.class)
            );
        }
    }
}
