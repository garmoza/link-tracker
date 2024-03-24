package edu.java.scrapper.service.impl.jdbc;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.exception.LinkAlreadyExistsException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import edu.java.scrapper.repository.SubscriptionRepository;
import edu.java.scrapper.repository.TgChatRepository;
import edu.java.scrapper.repository.TrackableLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import edu.java.scrapper.service.impl.JdbcSubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcSubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private TgChatRepository tgChatRepository;
    @Mock
    private TrackableLinkRepository trackableLinkRepository;
    @InjectMocks
    private JdbcSubscriptionService subscriptionService;

    @Test
    void getAllLinksByChatId() {
        when(subscriptionRepository.findAllByChatId(1L)).thenReturn(List.of(
            new Subscription(
                new Subscription.Id(1L, "https://example1.com"),
                OffsetDateTime.parse("2024-03-17T12:00:00Z")
            ),
            new Subscription(
                new Subscription.Id(1L, "https://example2.com"),
                OffsetDateTime.parse("2024-03-17T13:00:00Z")
            )
        ));

        var actual = subscriptionService.getAllLinksByChatId(1L);

        var links = List.of(
            new LinkResponse(1L, URI.create("https://example1.com")),
            new LinkResponse(1L, URI.create("https://example2.com"))
        );
        var body = ListLinksResponse.builder()
            .links(links)
            .size(2)
            .build();
        var expected = ResponseEntity.ok(body);
        assertEquals(expected, actual);
    }

    @Test
    void subscribeLink_NewSubscription() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.of(new TgChat(1L)));
        when(trackableLinkRepository.findByUrl("https://example.com")).thenReturn(Optional.of(
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        ));
        when(subscriptionRepository.existsByTgChatAndTrackableLink(any(), any())).thenReturn(false);
        when(subscriptionRepository.subscribe(any())).thenReturn(
            Subscription.builder()
                .id(new Subscription.Id(1L, "https://example.com"))
                .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        );

        var actual = subscriptionService.subscribeLink(1L, new AddLinkRequest("https://example.com"));

        var body = LinkResponse.builder()
            .id(1L)
            .url(URI.create("https://example.com"))
            .build();
        var expected = ResponseEntity.ok(body);
        assertEquals(expected, actual);
    }

    @Test
    void subscribeLink_SubscriptionAlreadyExists() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.of(new TgChat(1L)));
        when(trackableLinkRepository.findByUrl("https://example.com")).thenReturn(Optional.of(
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        ));
        when(subscriptionRepository.existsByTgChatAndTrackableLink(any(), any())).thenReturn(true);

        assertThrows(
            LinkAlreadyExistsException.class,
            () -> subscriptionService.subscribeLink(1L, new AddLinkRequest("https://example.com"))
        );
    }

    @Test
    void subscribeLink_ChatNotFound() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
            TgChatNotFoundException.class,
            () -> subscriptionService.subscribeLink(1L, new AddLinkRequest("https://example.com"))
        );
    }

    @Test
    void subscribeLink_LinkNotTrackableYet() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.of(new TgChat(1L)));
        when(trackableLinkRepository.findByUrl("https://example.com")).thenReturn(Optional.empty());
        when(trackableLinkRepository.add(any())).thenReturn(TrackableLink.builder()
            .url("https://example.com")
            .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build()
        );
        when(subscriptionRepository.existsByTgChatAndTrackableLink(any(), any())).thenReturn(false);
        when(subscriptionRepository.subscribe(any())).thenReturn(
            Subscription.builder()
                .id(new Subscription.Id(1L, "https://example.com"))
                .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        );

        var actual = subscriptionService.subscribeLink(1L, new AddLinkRequest("https://example.com"));

        var body = LinkResponse.builder()
            .id(1L)
            .url(URI.create("https://example.com"))
            .build();
        var expected = ResponseEntity.ok(body);
        assertEquals(expected, actual);
    }

    @Test
    void unsubscribeLink_ChatNotFound() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
            LinkNotFoundException.class,
            () -> subscriptionService.unsubscribeLink(1L, new RemoveLinkRequest("https://example.com"))
        );
    }

    @Test
    void unsubscribeLink_TrackableLinkNotFound() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.of(new TgChat(1L)));
        when(trackableLinkRepository.findByUrl("https://example.com")).thenReturn(Optional.empty());

        assertThrows(
            LinkNotFoundException.class,
            () -> subscriptionService.unsubscribeLink(1L, new RemoveLinkRequest("https://example.com"))
        );
    }

    @Test
    void unsubscribeLink_SubscriptionNotFound() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.of(new TgChat(1L)));
        when(trackableLinkRepository.findByUrl("https://example.com")).thenReturn(Optional.of(
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        ));
        when(subscriptionRepository.findByTgChatAndTrackableLink(any(), any())).thenReturn(Optional.empty());

        assertThrows(
            LinkNotFoundException.class,
            () -> subscriptionService.unsubscribeLink(1L, new RemoveLinkRequest("https://example.com"))
        );
    }

    @Test
    void unsubscribeLink_SubscriptionFound() {
        when(tgChatRepository.findById(1L)).thenReturn(Optional.of(new TgChat(1L)));
        when(trackableLinkRepository.findByUrl("https://example.com")).thenReturn(Optional.of(
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        ));
        when(subscriptionRepository.findByTgChatAndTrackableLink(any(), any())).thenReturn(Optional.of(
            Subscription.builder()
                .id(new Subscription.Id(1L, "https://example.com"))
                .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        ));
        when(subscriptionRepository.unsubscribe(any())).thenReturn(Subscription.builder()
            .id(new Subscription.Id(1L, "https://example.com"))
            .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build()
        );

        var actual = subscriptionService.unsubscribeLink(1L, new RemoveLinkRequest("https://example.com"));

        var body = LinkResponse.builder()
            .id(1L)
            .url(URI.create("https://example.com"))
            .build();
        var expected = ResponseEntity.ok(body);
        assertEquals(expected, actual);
    }
}
