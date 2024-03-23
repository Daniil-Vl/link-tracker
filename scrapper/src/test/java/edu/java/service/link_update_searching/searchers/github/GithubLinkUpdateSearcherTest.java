package edu.java.service.link_update_searching.searchers.github;

import edu.java.client.github.GithubClient;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.github.GithubEventResponse;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GithubLinkUpdateSearcherTest {
    @Mock
    private LinkService linkService;
    @Mock
    private GithubClient githubClient;
    @InjectMocks
    private GithubLinkUpdateSearcher githubLinkUpdateSearcher;

    @Test
    void getUpdates() {
        URI url = URI.create("https://github.com/author/repository");
        OffsetDateTime updateAt = OffsetDateTime.now();
        OffsetDateTime lastActivityDate = OffsetDateTime.now();
        OffsetDateTime actualLastActivityDate = OffsetDateTime.now().plus(Duration.ofDays(1));
        LinkDto linkDto = new LinkDto(
            1L,
            url,
            updateAt,
            lastActivityDate
        );

        Mockito.when(githubClient.getLatestUpdates("author", "repository", 1)).thenReturn(
            List.of(
                new GithubEventResponse(
                    1L,
                    "PushEvent",
                    new GithubEventResponse.Actor(2L, "login"),
                    new GithubEventResponse.Repo(3L, "name"),
                    actualLastActivityDate
                )
            )
        );

        List<LinkUpdate> expectedResult = List.of(
            new LinkUpdate(
                1L,
                url.toString(),
                "New commit"
            )
        );

        List<LinkUpdate> actualResult = githubLinkUpdateSearcher.getUpdates(linkDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        Mockito.verify(linkService).markNewUpdate(linkDto.id(), actualLastActivityDate);
    }
}
