package edu.java.service.linkupdatesearching.searchers.stackoverflow;

import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StackOverFlowLinkUpdateSearcherTest {
    @Mock
    private LinkService linkService;
    @Mock
    private StackoverflowClient stackoverflowClient;
    @InjectMocks
    private StackOverFlowLinkUpdateSearcher stackOverFlowLinkUpdateSearcher;

    @Test
    void getUpdates() {
        Long questionId = 123L;
        URI url = URI.create("https://stackoverflow.com/questions/123/question-description");
        OffsetDateTime updateAt = OffsetDateTime.now();
        OffsetDateTime lastActivityDate = OffsetDateTime.now();
        OffsetDateTime actualLastActivityDate = OffsetDateTime.now().plus(Duration.ofDays(1));
        LinkDto linkDto = new LinkDto(
            questionId,
            url,
            updateAt,
            lastActivityDate
        );

        Mockito.when(stackoverflowClient.getQuestion(questionId)).thenReturn(
            new StackoverflowQuestionResponse(
                questionId,
                "title",
                actualLastActivityDate
            )
        );

        List<LinkUpdate> expectedResult = List.of(
            new LinkUpdate(
                questionId,
                url.toString(),
                "New Update"
            )
        );

        List<LinkUpdate> actualResult = stackOverFlowLinkUpdateSearcher.getUpdates(linkDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        Mockito.verify(linkService).markNewUpdate(linkDto.id(), actualLastActivityDate);
    }
}
