package edu.java.service.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import edu.java.scrapper.IntegrationTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import static org.assertj.core.api.Assertions.assertThat;

class ScrapperQueueProducerTest extends IntegrationTest {

    @Autowired
    private ScrapperQueueProducer producer;

    @Autowired
    private KafkaTestConsumer consumer;

    @Test
    void testKafkaContainerIsRunning() {
        assertThat(KAFKA.isRunning()).isTrue();
    }

    @Test
    void testLinkUpdateRequestSending() throws InterruptedException {
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            "url",
            "description"
        );

        producer.send(linkUpdate, List.of());

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        LinkUpdateRequest payload = consumer.getPayload();

        assertThat(messageConsumed).isTrue();
        assertThat(payload.id()).isEqualTo(linkUpdate.linkId());
        assertThat(payload.url()).isEqualTo(linkUpdate.url());
        assertThat(payload.description()).isEqualTo(linkUpdate.description());
    }

    @TestConfiguration
    @RequiredArgsConstructor
    public static class KafkaTestConfig {
        private final ApplicationConfig applicationConfig;

        @Bean
        @Primary
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
        public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                applicationConfig.kafka().bootstrapServers()
            );
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                applicationConfig.kafka().groupId()
            );
            return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(LinkUpdateRequest.class)
            );
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> kafkaListenerContainerFactory(
            ConsumerFactory<String, LinkUpdateRequest> consumerFactory
        ) {
            ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            return factory;
        }

        @Bean
        public KafkaTestConsumer kafkaTestConsumer() {
            return new KafkaTestConsumer();
        }
    }

    @Log4j2
    @Getter
    public static class KafkaTestConsumer {
        private CountDownLatch latch = new CountDownLatch(1);
        private LinkUpdateRequest payload;

        @KafkaListener(topics = "${app.kafka.topic-name}", containerFactory = "kafkaListenerContainerFactory")
        public void listen(LinkUpdateRequest request) {
            log.info("Received payload={} in KafkaTestConsumer", request);
            payload = request;
            latch.countDown();
        }
    }
}
