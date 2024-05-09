package edu.java.service.link_update_searching.searchers.stackoverflow;

import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.stackoverflow.StackoverflowQuestionAnswersResponse;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
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
class StackOverFlowLinkUpdateSearcherTest {
    @Mock
    private LinkService linkService;
    @Mock
    private StackoverflowClient stackoverflowClient;
    @InjectMocks
    private StackOverFlowLinkUpdateSearcher stackOverFlowLinkUpdateSearcher;

    @Test
    void getUpdates_returnValidUpdates() {
        Long questionId = 123L;
        String title = "question_title";
        URI url =
            URI.create("https://stackoverflow.com/questions/%s/restful-api-endpoint-using-spring".formatted(questionId));
        LinkDto linkDto = new LinkDto(
            questionId,
            url,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        OffsetDateTime actualLastActivityDate = OffsetDateTime.now().plus(Duration.ofDays(1));

        Mockito
            .when(stackoverflowClient.getQuestion(questionId))
            .thenReturn(
                new StackoverflowQuestionResponse(
                    questionId,
                    title,
                    actualLastActivityDate
                )
            );

        Mockito
            .when(stackoverflowClient.getAnswers(questionId))
            .thenReturn(new StackoverflowQuestionAnswersResponse(
                List.of(
                    new StackoverflowQuestionAnswersResponse.Answer(
                        new StackoverflowQuestionAnswersResponse.Answer.Owner("owner_name"),
                        actualLastActivityDate,
                        actualLastActivityDate
                    )
                )
            ));

        List<LinkUpdate> expectedUpdates = List.of(
            new LinkUpdate(questionId, url.toString(), "New answer from owner_name in question " + url.toString())
        );

        List<LinkUpdate> updates = stackOverFlowLinkUpdateSearcher.getUpdates(linkDto);

        assertThat(updates).isEqualTo(expectedUpdates);
        Mockito.verify(linkService).markNewUpdate(linkDto.id(), actualLastActivityDate);
    }

    @Test
    void givenNoUpdates_whenGetUpdates_thenReturnEmptyList() {
        Long questionId = 123L;
        String title = "question_title";
        URI url =
            URI.create("https://stackoverflow.com/questions/%s/restful-api-endpoint-using-spring".formatted(questionId));
        OffsetDateTime lastActivityDate = OffsetDateTime.now();
        LinkDto linkDto = new LinkDto(
            questionId,
            url,
            lastActivityDate,
            lastActivityDate
        );

        Mockito
            .when(stackoverflowClient.getQuestion(questionId))
            .thenReturn(
                new StackoverflowQuestionResponse(
                    questionId,
                    title,
                    lastActivityDate.minus(Duration.ofDays(1))
                )
            );

        List<LinkUpdate> updates = stackOverFlowLinkUpdateSearcher.getUpdates(linkDto);

        assertThat(updates).isEmpty();
    }
}
