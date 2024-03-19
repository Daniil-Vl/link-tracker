package edu.java.domain.jooq;

import edu.java.domain.LinkRepository;
import edu.java.dto.dao.LinkDto;
import edu.java.domain.jooq.generated.tables.Link;
import edu.java.domain.jooq.generated.tables.records.LinkRecord;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class LinkRepositoryJooqImpl implements LinkRepository {
    private final DSLContext dslContext;
    Link link = Link.LINK;

    @Override
    public LinkDto add(String url) {
        Result<LinkRecord> result = dslContext
            .insertInto(link)
            .set(link.URL, url)
            .set(link.UPDATED_AT, OffsetDateTime.now())
            .set(link.LAST_CHECK_TIME, OffsetDateTime.now())
            .returning(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .fetch();

        if (result.isEmpty()) {
            log.warn("Jooq link repository add query didn't return link");
            throw new DataAccessException("Cannot add link with url = %s".formatted(url));
        }

        log.info("Jooq link repository add query returning");
        log.info("\n" + result);
        LinkRecord linkRecord = result.getFirst();

        return linkRecordToLinkDto(linkRecord);
    }

    @Override
    public Optional<LinkDto> remove(Long linkId) {
        Result<LinkRecord> result = dslContext
            .delete(link)
            .where(link.ID.eq(linkId))
            .returning(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .fetch();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository remove by id query returning");
        log.info("\n" + result);

        LinkRecord linkRecord = result.getFirst();
        return Optional.of(
            linkRecordToLinkDto(linkRecord)
        );
    }

    @Override
    public Optional<LinkDto> remove(String url) {
        Result<LinkRecord> result = dslContext
            .delete(link)
            .where(link.URL.eq(url))
            .returning(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .fetch();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository remove by url query returning");
        log.info("\n" + result);

        LinkRecord linkRecord = result.getFirst();
        return Optional.of(
            linkRecordToLinkDto(linkRecord)
        );
    }

    @Override
    public Optional<LinkDto> findById(Long linkId) {
        Result<Record> result = dslContext
            .select()
            .from(link)
            .where(link.ID.eq(linkId))
            .fetch();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository findById query returning");
        log.info("\n" + result);

        LinkRecord linkRecord = (LinkRecord) result.getFirst();
        return Optional.of(
            linkRecordToLinkDto(linkRecord)
        );
    }

    @Override
    public Optional<LinkDto> findByUrl(String url) {
        Result<Record> result = dslContext
            .select()
            .from(link)
            .where(link.URL.eq(url))
            .fetch();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq link repository findByUrl query returning");
        log.info("\n" + result);

        LinkRecord linkRecord = (LinkRecord) result.getFirst();
        return Optional.of(
            linkRecordToLinkDto(linkRecord)
        );
    }

    @Override
    public List<LinkDto> findAllById(Set<Long> setOfLinksId) {
        Result<Record> result = dslContext
            .select()
            .from(link)
            .where(link.ID.in(setOfLinksId))
            .fetch();

        log.info("Jooq link repository findAllById query returning");
        log.info("\n" + result);

        return result.into(LinkDto.class);
    }

    @Override
    public List<LinkDto> findAll() {
        Result<Record> result = dslContext
            .select()
            .from(link)
            .fetch();

        log.info("Jooq link repository findAll query returning");
        log.info("\n" + result);

        return result.into(LinkDto.class);
    }

    @Override
    public List<LinkDto> findAll(Duration interval) {
        Result<Record> result = dslContext
            .select()
            .from(link)
            .where(link.LAST_CHECK_TIME.lt(OffsetDateTime.now().minus(interval)))
            .fetch();

        log.info("Jooq link repository findAll with duration query returning");
        log.info("\n" + result);

        return result.into(LinkDto.class);
    }

    @Override
    public int markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt) {
        int rowsAffected = dslContext
            .update(link)
            .set(link.UPDATED_AT, newUpdatedAt)
            .set(link.LAST_CHECK_TIME, newUpdatedAt)
            .execute();

        log.info("Jooq link repository markNewUpdate query rows affected: %s".formatted(rowsAffected));

        return rowsAffected;
    }

    @Override
    public int markNewCheck(Long linkId, OffsetDateTime newLastCheckTime) {
        int rowsAffected = dslContext
            .update(link)
            .set(link.LAST_CHECK_TIME, newLastCheckTime)
            .execute();

        log.info("Jooq link repository markNewCheck query rows affected: %s".formatted(rowsAffected));

        return rowsAffected;
    }

    private LinkDto linkRecordToLinkDto(LinkRecord linkRecord) {
        return new LinkDto(
            linkRecord.getId(),
            URI.create(linkRecord.getUrl()),
            linkRecord.getUpdatedAt(),
            linkRecord.getLastCheckTime()
        );
    }
}
