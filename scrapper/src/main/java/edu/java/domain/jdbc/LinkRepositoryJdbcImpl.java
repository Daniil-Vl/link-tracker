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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
@SuppressWarnings("MultipleStringLiterals")
public class LinkRepositoryJdbcImpl implements LinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public LinkDto add(String url) {
        try {
            return jdbcClient
                .sql("INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?) RETURNING *")
                .param(url)
                .param(OffsetDateTime.now())
                .param(OffsetDateTime.now())
                .query(new LinkRowMapper())
                .single();
        } catch (DuplicateKeyException e) {
            log.warn("Try to add existing url = %s".formatted(url));
            return findByUrl(url).get();
        }
    }

    @Override
    public Optional<LinkDto> remove(Long linkId) {
        return jdbcClient
            .sql("DELETE FROM link WHERE id = ? RETURNING *")
            .param(linkId)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public Optional<LinkDto> remove(String url) {
        return jdbcClient
            .sql("DELETE FROM link WHERE url = ? RETURNING *")
            .param(url)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public Optional<LinkDto> findById(Long linkId) {
        return jdbcClient
            .sql("SELECT * FROM link WHERE id = ?")
            .param(linkId)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public Optional<LinkDto> findByUrl(String url) {
        return jdbcClient
            .sql("SELECT * FROM link WHERE url = ?")
            .param(url)
            .query(new LinkRowMapper())
            .optional();
    }

    @Override
    public List<LinkDto> findAllById(Set<Long> setOfLinksId) {
        return jdbcClient
            .sql("SELECT * FROM link WHERE id IN (:ids)")
            .param("ids", setOfLinksId.stream().toList())
            .query(new LinkRowMapper())
            .list();
    }

    @Override
    public List<LinkDto> findAll() {
        return jdbcClient
            .sql("SELECT * FROM link")
            .query(new LinkRowMapper())
            .list();
    }

    @Override
    public List<LinkDto> findAll(Duration interval) {
        return jdbcClient
            .sql("SELECT * FROM link WHERE last_check_time < ?")
            .param(OffsetDateTime.now().minus(interval))
            .query(new LinkRowMapper())
            .list();
    }

    @Override
    public int markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt) {
        return jdbcClient
            .sql("UPDATE link SET updated_at = ?, last_check_time = ? WHERE id = ?")
            .param(newUpdatedAt)
            .param(newUpdatedAt)
            .param(linkId)
            .update();
    }

    @Override
    public int markNewCheck(List<Long> linkIds, OffsetDateTime newLastCheckTime) {
        return jdbcClient
            .sql("UPDATE link SET last_check_time = :last_check_time WHERE id IN (:ids)")
            .param("last_check_time", newLastCheckTime)
            .param("ids", linkIds)
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
