package edu.java.domain;

import edu.java.domain.jdbc.LinkRepositoryJdbcImpl;
import edu.java.dto.dao.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class LinkRepositoryTest extends IntegrationTest {
    @Autowired
    protected JdbcClient jdbcClient;
    protected LinkRepository linkRepository;

    @BeforeEach
    abstract public void initRepository();

    @Test
    void addNewLink() {
        String url = "url";
        LinkDto result = linkRepository.add(url);

        LinkDto retrieved = jdbcClient
            .sql("SELECT * FROM link WHERE url = ?")
            .param(url)
            .query(new LinkRepositoryJdbcImpl.LinkRowMapper())
            .single();

        assertThat(retrieved).isNotNull();
        assertThat(retrieved).isEqualTo(result);
    }

    @Test
    void addExistingLink() {
        String url = "url";

        LinkDto insertedLinkDto = jdbcClient
            .sql("INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?) RETURNING *")
            .param(url)
            .param(OffsetDateTime.now())
            .param(OffsetDateTime.now())
            .query(new LinkRepositoryJdbcImpl.LinkRowMapper())
            .single();

        LinkDto result = linkRepository.add(url);

        LinkDto retrieved = jdbcClient
            .sql("SELECT * FROM link WHERE url = ?")
            .param(url)
            .query(new LinkRepositoryJdbcImpl.LinkRowMapper())
            .single();

        assertThat(retrieved).isNotNull();
        assertThat(retrieved).isEqualTo(result);
        assertThat(retrieved).isEqualTo(insertedLinkDto);
    }

    @Test
    void removeByUrlExistingLink() {
        String url = "url";
        LinkDto insertedLink = linkRepository.add(url);

        Optional<LinkDto> removedLink = linkRepository.remove(url);

        assertThat(removedLink).isNotEmpty();
        assertThat(removedLink.get()).isEqualTo(insertedLink);
    }

    @Test
    void removeByUrlNonExistingLink() {
        String url = "url";

        Optional<LinkDto> removedLink = linkRepository.remove(url);

        assertThat(removedLink).isEmpty();
    }

    @Test
    void removeExistingById() {
        LinkDto insertedLink = linkRepository.add("url");

        Optional<LinkDto> removedLink = linkRepository.remove(insertedLink.id());

        assertThat(removedLink).isNotEmpty();
        assertThat(removedLink.get()).isEqualTo(insertedLink);
    }

    @Test
    void removeNonExistingById() {
        Optional<LinkDto> removedLink = linkRepository.remove(1L);

        assertThat(removedLink).isEmpty();
    }

    @Test
    void findExistingLinkById() {
        LinkDto insertedLink = linkRepository.add("url");

        Optional<LinkDto> foundedLink = linkRepository.findById(insertedLink.id());

        assertThat(foundedLink).isNotEmpty();
        assertThat(foundedLink.get()).isEqualTo(insertedLink);
    }

    @Test
    void findNonExistingLinkById() {
        Optional<LinkDto> foundedLink = linkRepository.findById(1L);

        assertThat(foundedLink).isEmpty();
    }

    @Test
    void findExistingLinkByUrl() {
        LinkDto insertedLink = linkRepository.add("url");

        Optional<LinkDto> foundedLink = linkRepository.findByUrl(insertedLink.url().toString());

        assertThat(foundedLink).isNotEmpty();
        assertThat(foundedLink.get()).isEqualTo(insertedLink);
    }

    @Test
    void findNonExistingLinkByUrl() {
        Optional<LinkDto> foundedLink = linkRepository.findByUrl("url");

        assertThat(foundedLink).isEmpty();
    }

    @Test
    void findAllById() {
        List<String> urls = List.of("first", "second");
        List<LinkDto> linkDtos = urls.stream().map(url -> linkRepository.add(url)).toList();
        Set<Long> ids = linkDtos.stream().map(LinkDto::id).collect(Collectors.toSet());
        linkRepository.add("Third");

        List<LinkDto> result = linkRepository.findAllById(ids);

        assertThat(result).isEqualTo(linkDtos);
    }

    @Test
    void findAll() {
        List<String> urls = List.of("first", "second");
        List<LinkDto> linkDtos = urls.stream().map(url -> linkRepository.add(url)).toList();

        List<LinkDto> result = linkRepository.findAll();

        assertThat(result).isEqualTo(linkDtos);
    }

    @Test
    void findAllByLastCheckTime() {
        String firstUrl = "first";
        jdbcClient
            .sql("INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?)")
            .param(firstUrl)
            .param(OffsetDateTime.now())
            .param(OffsetDateTime.now())
            .update();

        String secondUrl = "second";
        jdbcClient
            .sql("INSERT INTO link(url, updated_at, last_check_time) VALUES (?, ?, ?)")
            .param(secondUrl)
            .param(OffsetDateTime.now().minus(Duration.ofDays(10)))
            .param(OffsetDateTime.now().minus(Duration.ofDays(10)))
            .update();

        List<LinkDto> result = linkRepository.findAll(Duration.ofDays(1));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().url().toString()).isEqualTo(secondUrl);
    }

    @Test
    void markNewUpdate() {
        String url = "url";
        LinkDto linkDto = linkRepository.add(url);

        linkRepository.markNewUpdate(linkDto.id(), linkDto.updatedAt().plus(Duration.ofDays(1)));

        LinkDto updatedLink = linkRepository.findByUrl(url).get();

        assertThat(updatedLink.updatedAt()).isAfter(linkDto.updatedAt());
        assertThat(updatedLink.lastCheckTime()).isAfter(linkDto.lastCheckTime());
    }

    @Test
    void markNewCheck() {
        String url = "url";
        LinkDto linkDto = linkRepository.add(url);

        linkRepository.markNewCheck(linkDto.id(), linkDto.updatedAt().plus(Duration.ofDays(1)));

        LinkDto updatedLink = linkRepository.findByUrl(url).get();

        assertThat(updatedLink.lastCheckTime()).isAfter(linkDto.lastCheckTime());
    }
}
