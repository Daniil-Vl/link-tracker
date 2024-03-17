package edu.java.service.jdbc;

import edu.java.dao.jdbc.LinkRepositoryJdbcImpl;
import edu.java.dao.jdbc.SubscriptionRepositoryJdbcImpl;
import edu.java.dto.dao.LinkDto;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceJdbcImpl implements LinkService {
    private final LinkRepositoryJdbcImpl linkRepositoryJdbc;
    private final SubscriptionRepositoryJdbcImpl subscriptionRepositoryJdbc;
    private final TgChatServiceJdbcImpl tgChatServiceJdbc;

    @Override
    public LinkDto add(long tgChatId, URI url) throws ChatNotExistException {
        if (!tgChatServiceJdbc.isAuthenticated(tgChatId)) {
            throw new ChatNotExistException();
        }

        Optional<LinkDto> res = linkRepositoryJdbc.findByUrl(url.toString());
        LinkDto linkDto = res.orElseGet(() -> linkRepositoryJdbc.add(url.toString()));

        subscriptionRepositoryJdbc.subscribe(tgChatId, linkDto.id());
        return linkDto;
    }

    @Override
    public LinkDto remove(long tgChatId, URI url) throws ChatNotExistException, LinkNotExistException {
        if (!tgChatServiceJdbc.isAuthenticated(tgChatId)) {
            throw new ChatNotExistException();
        }

        Optional<LinkDto> res = linkRepositoryJdbc.findByUrl(url.toString());
        if (res.isEmpty()) {
            throw new LinkNotExistException();
        }
        LinkDto linkDto = res.get();

        subscriptionRepositoryJdbc.unsubscribe(tgChatId, linkDto.id());

        return linkDto;
    }

    @Override
    public List<LinkDto> listAll(long tgChatId) {
        return subscriptionRepositoryJdbc.getAllSubscriptions(tgChatId);
    }
}
