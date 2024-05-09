package edu.java.bot.kafka.error_handlers;

import edu.java.LinkUpdateRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Log4j2
@RequiredArgsConstructor
public class KafkaErrorHandler implements CommonErrorHandler {
    private final KafkaTemplate<String, LinkUpdateRequest> dltKafkaTemplate;

    @Override
    public void handleOtherException(
        Exception thrownException,
        Consumer<?, ?> consumer,
        MessageListenerContainer container,
        boolean batchListener
    ) {
        log.info("handleOtherException invocation");
        handle(thrownException, consumer, Optional.empty());
    }

    @Override
    public boolean handleOne(
        Exception thrownException,
        ConsumerRecord<?, ?> consumerRecord,
        Consumer<?, ?> consumer,
        MessageListenerContainer container
    ) {
        log.info("handleOne invocation");
        handle(thrownException, consumer, Optional.of(consumerRecord));
        return true;
    }

    private void handle(
        Exception exception,
        Consumer<?, ?> consumer,
        Optional<ConsumerRecord<?, ?>> optionalConsumerRecord
    ) {
        log.error("Exception thrown = {}", exception.getClass());
        log.error("Exception message = {}", exception.getMessage());
        log.error("Exception cause = {}", exception.getCause().toString());
        log.info("Received consumer record optional = {} in error handler", optionalConsumerRecord);

        if (optionalConsumerRecord.isPresent()) {
            ConsumerRecord<?, ?> consumerRecord = optionalConsumerRecord.get();
            LinkUpdateRequest value = (LinkUpdateRequest) consumerRecord.value();
            String deadLetterTopic = consumerRecord.topic() + "_dlq";

            dltKafkaTemplate.send(
                deadLetterTopic,
                value
            );

            log.info("Send invalid link update request to topic = {}", deadLetterTopic);
        }

        if (exception instanceof RecordDeserializationException ex) {
            consumer.seek(ex.topicPartition(), ex.offset() + 1);
            consumer.commitSync();
        }

        log.info("Exception processed");
    }
}
