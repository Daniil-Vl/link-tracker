package edu.java.service;

import edu.java.domain.LinkRepository;
import edu.java.domain.SubscriptionRepository;
import edu.java.dto.dao.LinkDto;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {
    private final Long tgChatId = 1L;
    private final Long linkId = 2L;
    private final URI url = URI.create("url");
    private final LinkDto linkDto = new LinkDto(1L, url, OffsetDateTime.now(), OffsetDateTime.now());
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private TgChatService tgChatService;
    @InjectMocks
    private LinkServiceImpl linkService;

    @Test
    void addExistingLinkForAuthenticatedUser() {
        Mockito.when(tgChatService.isAuthenticated(tgChatId)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url.toString())).thenReturn(Optional.of(linkDto));

        LinkDto result = linkService.add(tgChatId, url);

        assertThat(result).isEqualTo(linkDto);
        Mockito.verify(tgChatService, Mockito.times(0)).register(tgChatId);
        Mockito.verify(subscriptionRepository).subscribe(tgChatId, linkDto.id());
    }

    @Test
    void addExistingLinkForNonAuthenticatedUser_thenRegister() {
        Mockito.when(tgChatService.isAuthenticated(tgChatId)).thenReturn(false);
        Mockito.when(linkRepository.findByUrl(url.toString())).thenReturn(Optional.of(linkDto));

        LinkDto result = linkService.add(tgChatId, url);

        assertThat(result).isEqualTo(linkDto);
        Mockito.verify(tgChatService).register(tgChatId);
        Mockito.verify(subscriptionRepository).subscribe(tgChatId, linkDto.id());
    }

    @Test
    void addNewLink_thenAddNewLinkToDb() {
        Mockito.when(tgChatService.isAuthenticated(tgChatId)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url.toString())).thenReturn(Optional.empty());
        Mockito.when(linkRepository.add(url.toString())).thenReturn(linkDto);

        LinkDto result = linkService.add(tgChatId, url);

        assertThat(result).isEqualTo(linkDto);
        Mockito.verify(linkRepository).add(url.toString());
        Mockito.verify(subscriptionRepository).subscribe(tgChatId, linkDto.id());
    }

    @Test
    void removeLinkForNonAuthenticatedUser_thenThrowsException() {
        Mockito.when(tgChatService.isAuthenticated(tgChatId)).thenReturn(false);

        assertThatThrownBy(() -> linkService.remove(tgChatId, url)).isInstanceOf(ChatNotExistException.class);
    }

    @Test
    void removeNonExistingLink_thenThrowException() {
        Mockito.when(tgChatService.isAuthenticated(tgChatId)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.remove(tgChatId, url)).isInstanceOf(LinkNotExistException.class);
    }

    @Test
    void removeLinkForAuthenticatedUser_thenRemoveSubscription() throws ChatNotExistException, LinkNotExistException {
        Mockito.when(tgChatService.isAuthenticated(tgChatId)).thenReturn(true);
        Mockito.when(linkRepository.findByUrl(url.toString())).thenReturn(Optional.of(linkDto));

        LinkDto result = linkService.remove(tgChatId, url);

        assertThat(result).isEqualTo(linkDto);
        Mockito.verify(subscriptionRepository).unsubscribe(tgChatId, linkDto.id());
    }

    @Test
    void listAll_thenCallSubscriptionRepositoryMethod() {
        linkService.listAll(tgChatId);

        Mockito.verify(subscriptionRepository).getAllSubscriptions(tgChatId);
    }

    @Test
    void markNewUpdate_thenCallLinkRepositoryMethod() {
        OffsetDateTime newUpdatedAt = OffsetDateTime.now();
        linkService.markNewUpdate(linkId, newUpdatedAt);

        Mockito.verify(linkRepository).markNewUpdate(linkId, newUpdatedAt);
    }

    @Test
    void markNewCheck_thenCallLinkRepositoryMethod() {
        OffsetDateTime newLastCheckTime = OffsetDateTime.now();
        linkService.markNewCheck(linkId, newLastCheckTime);

        Mockito.verify(linkRepository).markNewCheck(linkId, newLastCheckTime);
    }

    @Test
    void getAllSubscribers_thenCallSubscriptionRepository() {
        linkService.getAllSubscribers(linkId);

        Mockito.verify(subscriptionRepository).getAllSubscribers(linkId);
    }
}
