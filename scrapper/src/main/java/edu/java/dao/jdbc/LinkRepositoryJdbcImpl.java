package edu.java.dao.jdbc;

import edu.java.dao.LinkRepository;
import edu.java.dto.dao.LinkDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class LinkRepositoryJdbcImpl implements LinkRepository {
    private static final String ADD_LINK_QUERY = "INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?)";
    private static final String REMOVE_LINK_QUERY_BY_ID = "DELETE FROM link WHERE id = ?";
    private static final String REMOVE_LINK_QUERY_BY_URL = "DELETE FROM link WHERE url = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM link";
    private static final String FIND_ALL_QUERY_BY_TIME = "SELECT * FROM link WHERE last_check_time < ?";

    private final JdbcClient jdbcClient;

    @Override
    public int add(String url) {
        int rowsAffected = jdbcClient
            .sql(ADD_LINK_QUERY)
            .param(url)
            .param(OffsetDateTime.now())
            .param(OffsetDateTime.now())
            .update();

        log.info("Rows added: %s".formatted(rowsAffected));
        return rowsAffected;
    }

    @Override
    public int remove(Long linkId) {
        int rowsAffected = jdbcClient
            .sql(REMOVE_LINK_QUERY_BY_ID)
            .param(linkId)
            .update();

        log.info("Rows with id = %s removed: %s".formatted(linkId, rowsAffected));
        return rowsAffected;
    }

    @Override
    public int remove(String url) {
        int rowsAffected = jdbcClient
            .sql(REMOVE_LINK_QUERY_BY_URL)
            .param(url)
            .update();

        log.info("Rows with url = %s removed: %s".formatted(url, rowsAffected));
        return rowsAffected;
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

    private static final class LinkRowMapper implements RowMapper<LinkDto> {
        @Override
        public LinkDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LinkDto(
                rs.getString("url"),
                rs.getObject("updated_at", OffsetDateTime.class),
                rs.getObject("last_check_time", OffsetDateTime.class)
            );
        }
    }
}
