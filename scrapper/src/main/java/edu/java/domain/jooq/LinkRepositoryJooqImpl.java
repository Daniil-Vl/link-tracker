package edu.java.domain.jooq;

import edu.java.domain.LinkRepository;
import edu.java.domain.jooq.generated.tables.Link;
import edu.java.domain.jooq.utils.LinkDtoRecordMapper;
import edu.java.dto.dao.LinkDto;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class LinkRepositoryJooqImpl implements LinkRepository {
    private final DSLContext dslContext;
    Link link = Link.LINK;

    @Override
    public LinkDto add(String url) {
        List<LinkDto> result;
        try {
            result = dslContext
                .insertInto(link)
                .set(link.URL, url)
                .set(link.UPDATED_AT, OffsetDateTime.now())
                .set(link.LAST_CHECK_TIME, OffsetDateTime.now())
                .returning(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
                .fetch(new LinkDtoRecordMapper());
        } catch (DuplicateKeyException e) {
            log.warn("Try to add existing url = %s".formatted(url));
            return findByUrl(url).get();
        }

        if (result.isEmpty()) {
            log.warn("Jooq link repository add query didn't return link");
            throw new DataAccessException("Cannot add link with url = %s".formatted(url));
        }

        log.info("Jooq link repository add query returning");
        log.info("\n" + result);

        return result.getFirst();
    }

    @Override
    public Optional<LinkDto> remove(Long linkId) {
        List<LinkDto> result = dslContext
            .delete(link)
            .where(link.ID.eq(linkId))
            .returning(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .fetch(new LinkDtoRecordMapper());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository remove by id query returning");
        log.info("\n" + result);

        return Optional.of(result.getFirst());
    }

    @Override
    public Optional<LinkDto> remove(String url) {
        List<LinkDto> result = dslContext
            .delete(link)
            .where(link.URL.eq(url))
            .returning(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .fetch(new LinkDtoRecordMapper());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository remove by url query returning");
        log.info("\n" + result);

        return Optional.of(result.getFirst());
    }

    @Override
    public Optional<LinkDto> findById(Long linkId) {
        List<LinkDto> result = dslContext
            .select(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .from(link)
            .where(link.ID.eq(linkId))
            .fetch(new LinkDtoRecordMapper());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository findById query returning");
        log.info("\n" + result);

        return Optional.of(result.getFirst());
    }

    @Override
    public Optional<LinkDto> findByUrl(String url) {
        List<LinkDto> result = dslContext
            .select(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .from(link)
            .where(link.URL.eq(url))
            .fetch(new LinkDtoRecordMapper());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository findByUrl query returning");
        log.info("\n" + result);

        return Optional.of(result.getFirst());
    }

    @Override
    public List<LinkDto> findAllById(Set<Long> setOfLinksId) {
        List<LinkDto> result = dslContext
            .select(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .from(link)
            .where(link.ID.in(setOfLinksId))
            .fetch(new LinkDtoRecordMapper());

        log.info("Jooq link repository findAllById query returning");
        log.info("\n" + result);

        return result;
    }

    @Override
    public List<LinkDto> findAll() {
        List<LinkDto> result = dslContext
            .select(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .from(link)
            .fetch(new LinkDtoRecordMapper());

        log.info("Jooq link repository findAll query returning");
        log.info("\n" + result);

        return result;
    }

    @Override
    public List<LinkDto> findAll(Duration interval) {
        List<LinkDto> result = dslContext
            .select(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .from(link)
            .where(link.LAST_CHECK_TIME.lt(OffsetDateTime.now().minus(interval)))
            .fetch(new LinkDtoRecordMapper());

        log.info("Jooq link repository findAll by time query returning");
        log.info("\n" + result);

        return result;
    }

    @Override
    public int markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt) {
        int rowsAffected = dslContext
            .update(link)
            .set(link.UPDATED_AT, newUpdatedAt)
            .set(link.LAST_CHECK_TIME, newUpdatedAt)
            .where(link.ID.eq(linkId))
            .execute();

        log.info("Jooq link repository markNewUpdate query rows affected: %s".formatted(rowsAffected));

        return rowsAffected;
    }

    @Override
    public int markNewCheck(List<Long> linkIds, OffsetDateTime newLastCheckTime) {
        int rowsAffected = dslContext
            .update(link)
            .set(link.LAST_CHECK_TIME, newLastCheckTime)
            .where(link.ID.in(linkIds))
            .execute();

        log.info("Jooq link repository markNewCheck query rows affected: %s".formatted(rowsAffected));

        return rowsAffected;
    }
}
