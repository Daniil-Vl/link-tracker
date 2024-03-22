package edu.java.domain.jooq;

import edu.java.domain.SubscriptionRepository;
import edu.java.domain.jooq.generated.tables.Link;
import edu.java.domain.jooq.generated.tables.Subscription;
import edu.java.domain.jooq.generated.tables.records.SubscriptionRecord;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.dao.SubscriptionDto;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class SubscriptionRepositoryJooqImpl implements SubscriptionRepository {
    private final DSLContext dslContext;
    Subscription subscription = Subscription.SUBSCRIPTION;
    Link link = Link.LINK;

    @Override
    public SubscriptionDto subscribe(Long chatId, Long linkId) {
        Result<SubscriptionRecord> result;
        try {
            result = dslContext
                .insertInto(subscription)
                .set(subscription.CHAT_ID, chatId)
                .set(subscription.LINK_ID, linkId)
                .returning()
                .fetch();
        } catch (DuplicateKeyException e) {
            log.warn("Tried to add existing subscription (chat, link) = (%s, %s)".formatted(chatId, linkId));
            return new SubscriptionDto(chatId, linkId);
        }

        if (result.isEmpty()) {
            String errorMessage = "Jooq subscription repository subscribe query didn't return SubscriptionDto";
            log.warn(errorMessage);
            throw new DataAccessException(errorMessage);
        }

        log.info("Jooq subscription repository subscribe query returning");
        log.info("\n" + result);

        return subscriptionRecordToDto(result.getFirst());
    }

    @Override
    public Optional<SubscriptionDto> unsubscribe(Long chatId, Long linkId) {
        Result<SubscriptionRecord> result = dslContext
            .delete(subscription)
            .where(subscription.CHAT_ID.eq(chatId).and(subscription.LINK_ID.eq(linkId)))
            .returning()
            .fetch();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        log.info("Jooq subscription repository unsubscribe query returning");
        log.info("\n" + result);
        SubscriptionRecord subscriptionRecord = result.getFirst();

        return Optional.of(
            subscriptionRecordToDto(subscriptionRecord)
        );
    }

    @Override
    public List<Long> getAllSubscribers(Long linkId) {
        Result<Record1<Long>> result = dslContext
            .select(subscription.CHAT_ID)
            .from(subscription)
            .where(subscription.LINK_ID.eq(linkId))
            .fetch();

        log.info("Jooq subscription repository getAllSubscribers query returning");
        log.info("\n" + result);

        return result.into(Long.class);
    }

    @Override
    public List<LinkDto> getAllSubscriptions(Long chatId) {
        Result<Record4<Long, String, OffsetDateTime, OffsetDateTime>> result = dslContext
            .select(link.ID, link.URL, link.UPDATED_AT, link.LAST_CHECK_TIME)
            .from(subscription)
            .join(link).on(link.ID.eq(subscription.LINK_ID))
            .where(subscription.CHAT_ID.eq(chatId))
            .fetch();

        log.info("Jooq subscription repository getAllSubscriptions query returning");
        log.info("\n" + result);

        return result.into(LinkDto.class);
    }

    private SubscriptionDto subscriptionRecordToDto(SubscriptionRecord subscriptionRecord) {
        return new SubscriptionDto(
            subscriptionRecord.getChatId(),
            subscriptionRecord.getLinkId()
        );
    }
}
