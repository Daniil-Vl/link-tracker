package edu.java.service;

import edu.java.domain.LinkRepository;
import edu.java.domain.SubscriptionRepository;
import edu.java.dto.dao.LinkDto;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TgChatService tgChatService;

    @Override
    public LinkDto add(long tgChatId, URI url) {
        if (!tgChatService.isAuthenticated(tgChatId)) {
            tgChatService.register(tgChatId);
        }

        Optional<LinkDto> res = linkRepository.findByUrl(url.toString());
        LinkDto linkDto = res.orElseGet(() -> linkRepository.add(url.toString()));

        subscriptionRepository.subscribe(tgChatId, linkDto.id());
        return linkDto;
    }

    @Override
    public LinkDto remove(long tgChatId, URI url) throws ChatNotExistException, LinkNotExistException {
        if (!tgChatService.isAuthenticated(tgChatId)) {
            throw new ChatNotExistException();
        }

        Optional<LinkDto> res = linkRepository.findByUrl(url.toString());
        if (res.isEmpty()) {
            throw new LinkNotExistException("Cannot remove non-existent link");
        }
        LinkDto linkDto = res.get();

        subscriptionRepository.unsubscribe(tgChatId, linkDto.id());

        return linkDto;
    }

    @Override
    public List<LinkDto> listAll(long tgChatId) {
        return subscriptionRepository.getAllSubscriptions(tgChatId);
    }

    @Override
    public void markNewUpdate(Long linkId, OffsetDateTime newUpdatedAt) {
        linkRepository.markNewUpdate(linkId, newUpdatedAt);
    }

    @Override
    public int markNewCheck(Long linkId, OffsetDateTime newLastCheckTime) {
        return linkRepository.markNewCheck(linkId, newLastCheckTime);
    }

    @Override
    public List<Long> getAllSubscribers(Long linkId) {
        return subscriptionRepository.getAllSubscribers(linkId);
    }
}
