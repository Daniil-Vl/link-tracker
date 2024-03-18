package edu.java.domain.jdbc;

import edu.java.domain.LinkRepository;
import edu.java.dto.dao.LinkDto;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class LinkRepositoryJdbcImpl implements LinkRepository {
    private static final String ADD_LINK_QUERY =
        "INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?) RETURNING *";
    private static final String REMOVE_LINK_QUERY_BY_ID = "DELETE FROM link WHERE id = ? RETURNING *";
    private static final String REMOVE_LINK_QUERY_BY_URL = "DELETE FROM link WHERE url = ? RETURNING *";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM link WHERE id = ?";
    private static final String FIND_BY_ID_URL = "SELECT * FROM link WHERE url = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM link";
    private static final String FIND_ALL_QUERY_BY_TIME = "SELECT * FROM link WHERE last_check_time < ?";
    private static final String FIND_ALL_BY_ID = "SELECT * FROM link WHERE id IN (?)";
    private static final String MARK_NEW_UPDATE_QUERY =
        "UPDATE link SET updated_at = ?, last_check_time = ? WHERE id = ?";
    private static final String MARK_NEW_CHECK_TIME = "UPDATE link SET last_check_time = ? WHERE id = ?";

    private final JdbcClient jdbcClient;

    @Override
    public LinkDto add(String url) {
        return jdbcClient
            .sql(ADD_LINK_QUERY)
            .param(url)
            .param(OffsetDateTime.now())
            .param(OffsetDateTime.now())
            .query(new LinkRowMapper())
            .single();
    }

    @Override
    public Optional<LinkDto> remove(Long linkId) {
        return jdbcClient
            .sql(REMOVE_LINK_QUERY_BY_ID)
            .param(linkId)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public Optional<LinkDto> remove(String url) {
        return jdbcClient
            .sql(REMOVE_LINK_QUERY_BY_URL)
            .param(url)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public Optional<LinkDto> findById(Long linkId) {
        return jdbcClient
            .sql(FIND_BY_ID_QUERY)
            .param(linkId)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public Optional<LinkDto> findByUrl(String url) {
        return jdbcClient
            .sql(FIND_BY_ID_URL)
            .param(url)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public List<LinkDto> findAllById(Set<Long> setOfLinksId) {
        return jdbcClient
            .sql(FIND_ALL_BY_ID)
            .param(setOfLinksId.toString().substring(1, setOfLinksId.toString().length() - 1))
            .query(new LinkRowMapper())
            .list();
    }

    @Override
    public List<LinkDto> findAll() {
        return jdbcClient
            .sql(FIND_ALL_QUERY)
            .query(new LinkRowMapper())
            .list();
    }

    @Override
    public List<LinkDto> findAll(Duration interval) {
        return jdbcClient
            .sql(FIND_ALL_QUERY_BY_TIME)
            .param(OffsetDateTime.now().minus(interval))
            .query(new LinkRowMapper())
            .list();
    }

    @Override
    public int markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt) {
        return jdbcClient
            .sql(MARK_NEW_UPDATE_QUERY)
            .param(newUpdatedAt)
            .param(newUpdatedAt)
            .param(linkId)
            .update();
    }

    @Override
    public int markNewCheck(Long linkId, OffsetDateTime newLastCheckTime) {
        return jdbcClient
            .sql(MARK_NEW_CHECK_TIME)
            .param(newLastCheckTime)
            .param(linkId)
            .update();
    }

    public static final class LinkRowMapper implements RowMapper<LinkDto> {
        @Override
        public LinkDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LinkDto(
                rs.getLong("id"),
                URI.create(rs.getString("url")),
                rs.getObject("updated_at", OffsetDateTime.class),
                rs.getObject("last_check_time", OffsetDateTime.class)
            );
        }
    }
}
