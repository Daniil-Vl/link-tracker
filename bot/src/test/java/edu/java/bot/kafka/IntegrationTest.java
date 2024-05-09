package edu.java.bot.kafka;

import edu.java.bot.telegram.bot.Bot;
import io.micrometer.core.instrument.Counter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
public class IntegrationTest {
    protected static KafkaContainer KAFKA = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.3.2")
    );

    static {
        KAFKA.start();
    }

    // Mock prometheus counter, because spring boot test cannot find PrometheusMeterRegistry bean definition
    @MockBean
    private Counter proccesedMessagesCounter;

    // Mock Bot dean to prevent request sending to telegram api
    @MockBean
    private Bot bot;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("app.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("app.kafka.topic-name", () -> "test-topic");
        registry.add("app.kafka.group-id", () -> "test-group-id");
    }

    @DynamicPropertySource
    static void portProperty(DynamicPropertyRegistry registry) {
        registry.add("server.port", () -> 8082);
    }
}
