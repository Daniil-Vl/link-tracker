package edu.java.bot.configuration.kafka;

import edu.java.LinkUpdateRequest;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class DltConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public NewTopic dlt() {
        ApplicationConfig.Kafka kafkaConfig = applicationConfig.kafka();
        return TopicBuilder
            .name(kafkaConfig.topicName() + "_dlq")
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> dltProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            applicationConfig.kafka().bootstrapServers()
        );
        return new DefaultKafkaProducerFactory<>(
            props,
            new StringSerializer(),
            new JsonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> dltKafkaTemplate(
        ProducerFactory<String, LinkUpdateRequest> dltProducerFactory
    ) {
        return new KafkaTemplate<>(dltProducerFactory);
    }
}
