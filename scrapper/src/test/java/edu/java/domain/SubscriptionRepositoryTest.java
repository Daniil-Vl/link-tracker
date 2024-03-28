package edu.java.domain;

import edu.java.domain.jdbc.LinkRepositoryJdbcImpl;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.dao.SubscriptionDto;
import edu.java.scrapper.IntegrationTest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class SubscriptionRepositoryTest extends IntegrationTest {
    private final Long chatId = 1L;
    @Autowired
    protected JdbcClient jdbcClient;
    protected SubscriptionRepository subscriptionRepository;
    private LinkDto linkDto;
    private SubscriptionDto subscriptionDto;

    @BeforeEach
    abstract public void initRepository();

    void initTables() {
        String url = "url";

        jdbcClient
            .sql("INSERT INTO chat(chat_id) VALUES (?)")
            .param(chatId)
            .update();

        linkDto = jdbcClient
            .sql("INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?) RETURNING *")
            .param(url)
            .param(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
            .param(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
            .query(new LinkRepositoryJdbcImpl.LinkRowMapper())
            .single();

        subscriptionDto = new SubscriptionDto(chatId, linkDto.id());
    }

    @Test
    void createNewSubscription() {
        initTables();
        SubscriptionDto result = subscriptionRepository.subscribe(chatId, linkDto.id());

        SubscriptionDto retrievedSubscription = jdbcClient
            .sql("SELECT * FROM subscription WHERE chat_id = ? AND link_id = ?")
            .param(chatId)
            .param(linkDto.id())
            .query(new TestSubscriptionRowMapper())
            .single();

        assertThat(result).isEqualTo(subscriptionDto);
        assertThat(retrievedSubscription).isEqualTo(subscriptionDto);
    }

    @Test
    void createExistingSubscription() {
        initTables();
        jdbcClient
            .sql("INSERT INTO subscription(chat_id, link_id) VALUES (?, ?)")
            .param(chatId)
            .param(linkDto.id())
            .update();

        SubscriptionDto result = subscriptionRepository.subscribe(chatId, linkDto.id());

        SubscriptionDto retrievedSubscription = jdbcClient
            .sql("SELECT * FROM subscription WHERE chat_id = ? AND link_id = ?")
            .param(chatId)
            .param(linkDto.id())
            .query(new TestSubscriptionRowMapper())
            .single();

        assertThat(result).isEqualTo(subscriptionDto);
        assertThat(retrievedSubscription).isEqualTo(subscriptionDto);
    }

    @Test
    void removeExistingSubscription() {
        initTables();
        subscriptionRepository.subscribe(chatId, linkDto.id());

        Optional<SubscriptionDto> result = subscriptionRepository.unsubscribe(chatId, linkDto.id());

        assertThat(result).contains(subscriptionDto);
    }

    @Test
    void removeNonExistingSubscription() {
        initTables();
        Optional<SubscriptionDto> result = subscriptionRepository.unsubscribe(chatId, linkDto.id());

        assertThat(result).isEmpty();
    }

    @Test
    void getAllSubscribers() {
        initTables();
        List<Long> subscribers = List.of(1L);

        for (Long chatId : subscribers) {
            subscriptionRepository.subscribe(chatId, linkDto.id());
        }

        List<Long> actualList = subscriptionRepository.getAllSubscribers(linkDto.id());

        assertThat(actualList).isEqualTo(subscribers);
    }

    @Test
    void getAllSubscriptions() {
        initTables();
        List<LinkDto> subscriptions = List.of(linkDto);

        for (LinkDto linkDto : subscriptions) {
            subscriptionRepository.subscribe(chatId, linkDto.id());
        }

        List<LinkDto> actualList = subscriptionRepository.getAllSubscriptions(chatId);

        assertThat(actualList).isEqualTo(subscriptions);
    }

    private static final class TestSubscriptionRowMapper implements RowMapper<SubscriptionDto> {
        @Override
        public SubscriptionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SubscriptionDto(
                rs.getLong("chat_id"),
                rs.getLong("link_id")
            );
        }
    }
}
