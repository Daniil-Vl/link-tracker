package edu.java.controller;

import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@SuppressWarnings("MultipleStringLiterals")
public class ScrapperControllerImpl implements ScrapperController {
    @Override
    public String registerChat(Integer id) {
        return "Chat has been successfully registered";
    }

    @Override
    public String deleteChat(Integer id) {
        return "Chat was successfully deleted";
    }

    @Override
    public ListLinksResponse getLinks(Integer tgChatId) {
        return new ListLinksResponse(
            List.of(new ListLinksResponse.LinkResponse(1, "url")),
            1
        );
    }

    @Override
    public LinkResponse addLink(Integer tgChatId, AddLinkRequest addLinkRequest) {
        return new LinkResponse(1, "url");
    }

    @Override
    public LinkResponse deleteLinks(Integer tgChatId, RemoveLinkRequest removeLinkRequest) {
        return new LinkResponse(1, "url");
    }
}
