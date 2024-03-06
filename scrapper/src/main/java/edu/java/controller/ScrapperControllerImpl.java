package edu.java.controller;

import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ScrapperControllerImpl implements ScrapperController {
    @Override
    public String registerChat(Integer id) {
        return "Chat %s has been successfully registered".formatted(id);
    }

    @Override
    public String deleteChat(Integer id) {
        return "Chat %s was successfully deleted".formatted(id);
    }

    @Override
    public ListLinksResponse getLinks(Integer tgChatId) {
        return new ListLinksResponse(
            List.of(new ListLinksResponse.LinkResponse(1, URI.create("url"))),
            tgChatId
        );
    }

    @Override
    public LinkResponse addLink(Integer tgChatId, AddLinkRequest addLinkRequest) {
        return new LinkResponse(
            tgChatId,
            addLinkRequest.link()
        );
    }

    @Override
    public LinkResponse deleteLinks(Integer tgChatId, RemoveLinkRequest removeLinkRequest) {
        return new LinkResponse(
            tgChatId,
            removeLinkRequest.link()
        );
    }
}
