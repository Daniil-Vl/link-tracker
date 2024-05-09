package edu.java.service.link_update_searching.searchers.stackoverflow;

import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.stackoverflow.StackoverflowQuestionAnswersResponse;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import edu.java.service.LinkService;
import edu.java.service.link_update_searching.searchers.LinkUpdateSearcher;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackOverFlowLinkUpdateSearcher implements LinkUpdateSearcher {
    private static final String HOST_NAME = "stackoverflow.com";
    private final StackoverflowClient stackoverflowClient;
    private final LinkService linkService;

    @Override
    public String host() {
        return HOST_NAME;
    }

    @Override
    public List<LinkUpdate> getUpdates(LinkDto linkDto) {
        Long questionId = getQuestionId(linkDto.url());
        StackoverflowQuestionResponse response = stackoverflowClient.getQuestion(questionId);

        if (!response.lastActivityDate().isAfter(linkDto.updatedAt())) {
            return List.of();
        }

        linkService.markNewUpdate(
            linkDto.id(),
            response.lastActivityDate()
        );

        List<LinkUpdate> updates = processAnswers(questionId, linkDto);

        if (updates.isEmpty()) {
            return List.of(
                new LinkUpdate(
                    linkDto.id(),
                    linkDto.url().toString(),
                    "New updates"
                )
            );
        }

        return updates;
    }

    private List<LinkUpdate> processAnswers(Long questionId, LinkDto linkDto) {
        StackoverflowQuestionAnswersResponse answersResponse = stackoverflowClient.getAnswers(questionId);

        List<StackoverflowQuestionAnswersResponse.Answer> answers = answersResponse.answers().stream()
            .filter(answer -> answer.creationDate().isAfter(linkDto.updatedAt()))
            .toList();

        return answers.stream()
            .map(answer -> new LinkUpdate(
                linkDto.id(),
                linkDto.url().toString(),
                "New answer from " + answer.owner().name() + " in question " + linkDto.url().toString()
            ))
            .toList();
    }

    private Long getQuestionId(URI questionUrl) {
        return Long.valueOf(
            questionUrl.getPath().split("/")[2]
        );
    }
}
