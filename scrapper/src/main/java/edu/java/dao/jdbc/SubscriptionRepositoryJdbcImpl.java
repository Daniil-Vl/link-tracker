package edu.java.dao.jdbc;

import edu.java.dao.SubscriptionRepository;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.dao.SubscriptionDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class SubscriptionRepositoryJdbcImpl implements SubscriptionRepository {
    private static final String SUBSCRIBE_QUERY =
        "INSERT INTO subscription(chat_id, link_id) VALUES (?, ?) RETURNING *";
    private static final String UNSUBSCRIBE_QUERY =
        "DELETE FROM subscription WHERE chat_id = ? AND link_id = ? RETURNING *";
    private static final String SELECT_ALL_SUBSCRIBED_USERS =
        "SELECT chat_id FROM subscription WHERE link_id = ?";
    private static final String SELECT_ALL_SUBSCRIPTIONS =
        "SELECT l.id, l.url, l.updated_at, l.last_check_time from subscription s "
            + "JOIN link l on l.id = s.link_id WHERE chat_id = ?";

    private final JdbcClient jdbcClient;

    @Override
    public SubscriptionDto subscribe(Long chatId, Long linkId) {
        return jdbcClient
            .sql(SUBSCRIBE_QUERY)
            .param(chatId)
            .param(linkId)
            .query(new SubscriptionRowMapper())
            .single();
    }

    @Override
    public Optional<SubscriptionDto> unsubscribe(Long chatId, Long linkId) {
        return jdbcClient
            .sql(UNSUBSCRIBE_QUERY)
            .param(chatId)
            .param(linkId)
            .query(new SubscriptionRowMapper())
            .optional();
    }

    @Override
    public List<Long> getAllSubscribedUsers(Long linkId) {
        return jdbcClient
            .sql(SELECT_ALL_SUBSCRIBED_USERS)
            .param(linkId)
            .query(Long.class)
            .list();
    }

    @Override
    public List<LinkDto> getAllSubscriptions(Long chatId) {
        return jdbcClient
            .sql(SELECT_ALL_SUBSCRIPTIONS)
            .param(chatId)
            .query(new LinkRepositoryJdbcImpl.LinkRowMapper())
            .list();
    }

    private static final class SubscriptionRowMapper implements RowMapper<SubscriptionDto> {
        @Override
        public SubscriptionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SubscriptionDto(
                rs.getLong("chat_id"),
                rs.getLong("link_id")
            );
        }
    }
}
