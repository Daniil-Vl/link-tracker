package edu.java.scrapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
public abstract class IntegrationTest {

    protected static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();
    }

    @Autowired
    private JdbcClient jdbcClient;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @DynamicPropertySource
    static void liquibaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.liquibase.change-log", () -> "classpath:migrations/master.xml");
    }

    @DynamicPropertySource
    static void schedulerProperties(DynamicPropertyRegistry registry) {
        registry.add("app.scheduler.enable", () -> false);
    }

    @BeforeEach
    void clearDB() {
        jdbcClient
            .sql("DELETE FROM subscription")
            .update();

        jdbcClient
            .sql("DELETE FROM chat")
            .update();

        jdbcClient
            .sql("DELETE FROM link")
            .update();
    }
}
