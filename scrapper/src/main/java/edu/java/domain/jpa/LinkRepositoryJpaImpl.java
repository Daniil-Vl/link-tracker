package edu.java.domain.jpa;

import edu.java.domain.LinkRepository;
import edu.java.domain.jpa.entities.LinkEntity;
import edu.java.domain.jpa.jpa_repositories.JpaLinkRepository;
import edu.java.dto.dao.LinkDto;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
@Log4j2
public class LinkRepositoryJpaImpl implements LinkRepository {
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    public LinkDto add(String url) {
        LinkEntity link = new LinkEntity();
        link.setUrl(url);

        OffsetDateTime now = OffsetDateTime.now().withNano(0);
        link.setUpdatedAt(now);
        link.setLastCheckTime(now);

        LinkEntity insertedLink;
        try {
            insertedLink = jpaLinkRepository.saveAndFlush(link);
        } catch (DataIntegrityViolationException e) {
            log.warn("Tried to add existing link");
            insertedLink = jpaLinkRepository.findByUrl(link.getUrl()).get();
        }

        return entityToDto(insertedLink);
    }

    @Override
    public Optional<LinkDto> remove(Long linkId) {
        Optional<LinkEntity> linkEntityOptional = jpaLinkRepository.findById(linkId);

        if (linkEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        LinkEntity linkEntity = linkEntityOptional.get();
        jpaLinkRepository.delete(linkEntity);

        return Optional.of(entityToDto(linkEntity));
    }

    @Override
    public Optional<LinkDto> remove(String url) {
        Optional<LinkEntity> linkEntityOptional = jpaLinkRepository.findByUrl(url);

        if (linkEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        LinkEntity linkEntity = linkEntityOptional.get();
        jpaLinkRepository.delete(linkEntity);

        return Optional.of(entityToDto(linkEntity));
    }

    @Override
    public Optional<LinkDto> findById(Long linkId) {
        Optional<LinkEntity> optionalLinkEntity = jpaLinkRepository.findById(linkId);

        return optionalLinkEntity.map(this::entityToDto);
    }

    @Override
    public Optional<LinkDto> findByUrl(String url) {
        Optional<LinkEntity> optionalLinkEntity = jpaLinkRepository.findByUrl(url);

        return optionalLinkEntity.map(this::entityToDto);
    }

    @Override
    public List<LinkDto> findAllById(Set<Long> setOfLinksId) {
        List<LinkEntity> linkEntities = jpaLinkRepository.findAllById(setOfLinksId);
        return linkEntities
            .stream()
            .map(this::entityToDto)
            .toList();
    }

    @Override
    public List<LinkDto> findAll() {
        return jpaLinkRepository
            .findAll()
            .stream()
            .map(this::entityToDto)
            .toList();
    }

    @Override
    public List<LinkDto> findAll(Duration interval) {
        return jpaLinkRepository
            .findAllByLastCheckTimeBefore(OffsetDateTime.now().minus(interval))
            .stream()
            .map(this::entityToDto)
            .toList();
    }

    @Override
    public int markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt) {
        jpaLinkRepository.markNewUpdate(linkId, newUpdatedAt, newUpdatedAt);
        return 1;
    }

    @Override
    public int markNewCheck(List<Long> linkIds, OffsetDateTime newLastCheckTime) {
        jpaLinkRepository.markNewCheck(linkIds, newLastCheckTime);
        return linkIds.size();
    }

    private LinkDto entityToDto(LinkEntity linkEntity) {
        return new LinkDto(
            linkEntity.getId(),
            URI.create(linkEntity.getUrl()),
            linkEntity.getUpdatedAt().withOffsetSameInstant(ZoneOffset.UTC),
            linkEntity.getLastCheckTime().withOffsetSameInstant(ZoneOffset.UTC)
        );
    }
}
