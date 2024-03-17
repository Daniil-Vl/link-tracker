package edu.java.resource.stackoverflow;

import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;
import edu.java.resource.ResourceManager;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackOverFlowResourceManager implements ResourceManager {
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

        return List.of(
            processUpdate(response, linkDto.id(), linkDto.url())
        );
    }

    private LinkUpdate processUpdate(StackoverflowQuestionResponse response, Long resourceId, URI url) {
        linkService.markNewUpdate(
            resourceId,
            response.lastActivityDate()
        );

        return new LinkUpdate(
            resourceId,
            url.toString(),
            "New Update"
        );
    }

    private Long getQuestionId(URI questionUrl) {
        return Long.valueOf(
            questionUrl.getPath().split("/")[2]
        );
    }
}
