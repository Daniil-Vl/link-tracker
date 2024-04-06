package edu.java.bot.configuration;

import edu.java.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class KafkaConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public NewTopic topic() {
        ApplicationConfig.Kafka kafkaConfig = applicationConfig.kafka();
        return TopicBuilder
            .name(kafkaConfig.topicName())
            .partitions(kafkaConfig.partitions())
            .replicas(kafkaConfig.replicationFactor())
            .build();
    }

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
}
