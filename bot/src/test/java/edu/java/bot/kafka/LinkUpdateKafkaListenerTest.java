package edu.java.bot.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.LinkUpdateHandler;
import edu.java.bot.telegram.bot.Bot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class LinkUpdateKafkaListenerTest extends IntegrationTest {

    @Autowired
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @Autowired
    private ApplicationConfig applicationConfig;

    @MockBean
    private LinkUpdateHandler linkUpdateHandler;

    @Autowired
    private DltConsumer dltConsumer;

    // Mock Bot dean to prevent request sending to telegram api
    @MockBean
    private Bot bot;

    @BeforeEach
    void resetConsumerLatch() {
        dltConsumer.resetLatch();
    }

    void sendRequest(LinkUpdateRequest linkUpdateRequest)
        throws ExecutionException, InterruptedException, TimeoutException {
        kafkaTemplate.send(
            applicationConfig.kafka().topicName(),
            linkUpdateRequest
        ).get(10L, TimeUnit.SECONDS);
    }

    @Test
    void givenValidMessage_whenListen_thenProcessLinkUpdate()
        throws ExecutionException, InterruptedException, TimeoutException {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            "url",
            "description",
            List.of(1L)
        );
        sendRequest(linkUpdateRequest);

//        verify(linkUpdateHandler, timeout(10000).atLeastOnce()).processLinkUpdates(List.of(linkUpdateRequest));
        verify(linkUpdateHandler, timeout(10000).atLeastOnce()).processLinkUpdate(linkUpdateRequest);
    }

    @Test
    void givenInvalidMessage_whenListen_thenSendMessageToDeadLetterQueue()
        throws ExecutionException, InterruptedException, TimeoutException {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            "url",
            "description",
            List.of()
        );

        sendRequest(linkUpdateRequest);
        boolean messageConsumed = dltConsumer.getLatch().await(10, TimeUnit.SECONDS);
        LinkUpdateRequest payload = dltConsumer.getPayload();

        verify(linkUpdateHandler, never()).processLinkUpdate(linkUpdateRequest);
        assertThat(messageConsumed).isTrue();
        assertThat(payload).isEqualTo(linkUpdateRequest);
    }

    @Test
    void givenValidMessage_whenListenThrowException_thenSendMessageToDeadLetterQueue()
        throws ExecutionException, InterruptedException, TimeoutException {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            "url",
            "description",
            List.of(1L)
        );
        doThrow(RuntimeException.class)
            .when(linkUpdateHandler)
            .processLinkUpdate(linkUpdateRequest);

        sendRequest(linkUpdateRequest);
        boolean messageConsumed = dltConsumer.getLatch().await(10, TimeUnit.SECONDS);
        LinkUpdateRequest payload = dltConsumer.getPayload();

        verify(linkUpdateHandler).processLinkUpdate(linkUpdateRequest);
        assertThat(messageConsumed).isTrue();
        assertThat(payload).isEqualTo(linkUpdateRequest);
    }

    @TestConfiguration
    @RequiredArgsConstructor
    public static class KafkaTestConfig {
        private final ApplicationConfig applicationConfig;

        @Bean
        public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                applicationConfig.kafka().bootstrapServers()
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

        @Bean
        public DltConsumer kafkaTestConsumer() {
            return new DltConsumer();
        }
    }

    @Log4j2
    @Getter
    public static class DltConsumer {
        private CountDownLatch latch = new CountDownLatch(1);
        private LinkUpdateRequest payload;

        @KafkaListener(topics = "${app.kafka.topic-name}_dlq", containerFactory = "kafkaListenerContainerFactory")
        public void listen(@Payload LinkUpdateRequest request) {
            log.info("Received kafka dead message with payload = {} in KafkaTestConsumer", request);
            payload = request;
            latch.countDown();
        }

        public void resetLatch() {
            latch = new CountDownLatch(1);
        }
    }
}
