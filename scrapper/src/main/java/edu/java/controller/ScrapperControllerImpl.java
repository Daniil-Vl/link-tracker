package edu.java.controller;

import edu.java.dto.dao.LinkDto;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ScrapperControllerImpl implements ScrapperController {
    private final TgChatService tgChatService;
    private final LinkService linkService;

    @Override
    public String registerChat(Long id) {
        tgChatService.register(id);
        return "Chat has been successfully registered";
    }

    @Override
    public String deleteChat(Long id) throws ChatNotExistException {
        tgChatService.unregister(id);
        return "Chat has been successfully removed";
    }

    @Override
    public ListLinksResponse getLinks(Long tgChatId) {
        List<LinkDto> linkDtoList = linkService.listAll(tgChatId);

        List<LinkResponse> linkResponses = linkDtoList
            .stream()
            .map(linkDto -> {
                return new LinkResponse(linkDto.id(), linkDto.url());
            })
            .toList();

        return new ListLinksResponse(
            linkResponses,
            linkResponses.size()
        );
    }

    @Override
    public LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        LinkDto linkDto = linkService.add(tgChatId, addLinkRequest.link());

        return new LinkResponse(
            linkDto.id(),
            linkDto.url()
        );
    }

    @Override
    public LinkResponse deleteLinks(Long tgChatId, RemoveLinkRequest removeLinkRequest)
        throws ChatNotExistException, LinkNotExistException {
        LinkDto linkDto = linkService.remove(
            tgChatId,
            removeLinkRequest.link()
        );

        return new LinkResponse(
            linkDto.id(),
            linkDto.url()
        );
    }
}
