package edu.java.service.linkupdatesearching;

import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.linkupdatesearching.searchers.LinkUpdateSearcher;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SearchersManagerServiceImplTest {
    @Mock
    private LinkUpdateSearcher firstSearcher;
    @Mock
    private LinkUpdateSearcher secondSearcher;
    private SearchersManagerServiceImpl searchersManagerService;

    @BeforeEach
    void initService() {
        List<LinkUpdateSearcher> linkUpdateSearcherList = List.of(
            firstSearcher,
            secondSearcher
        );

        searchersManagerService = new SearchersManagerServiceImpl(linkUpdateSearcherList);
    }

    @Test
    @DisplayName("SearchersManagerServiceImpl chose the right searcher")
    void correctSearcherChoosing() {
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("url"),
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        List<LinkUpdate> linkUpdates = List.of(
            new LinkUpdate(linkDto.id(), linkDto.url().toString(), "correct description")
        );

        Mockito.when(firstSearcher.canProcess(linkDto.url())).thenReturn(false);
        Mockito.when(secondSearcher.canProcess(linkDto.url())).thenReturn(true);

        Mockito.when(secondSearcher.getUpdates(linkDto)).thenReturn(linkUpdates);

        List<LinkUpdate> expectedResult = List.of(
            new LinkUpdate(
                linkDto.id(),
                linkDto.url().toString(),
                "correct description"
            )
        );

        List<LinkUpdate> actualResult = searchersManagerService.getUpdates(linkDto);

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
