package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.TrackableLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SubscriptionRepositoryTest extends IntegrationTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private TgChatRepository tgChatRepository;
    @Autowired
    private TrackableLinkRepository trackableLinkRepository;

    @BeforeEach
    void setUp() {
        addTgChat(1L);
        addTrackableLink("https://example.com");
    }

    private void addTgChat(long chatId) {
        tgChatRepository.add(new TgChat(chatId));
    }

    private void addTrackableLink(String url) {
        trackableLinkRepository.add(TrackableLink.builder()
            .url(url)
            .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build());
    }

    @Test
    @Transactional
    @Rollback
    void subscribe() {
        subscriptionRepository.subscribe(testSubscription(1L, "https://example.com"));

        List<Subscription> actual = subscriptionRepository.findAllByChatId(1L);

        List<Subscription> expected = List.of(testSubscription(1L, "https://example.com"));
        assertEquals(expected, actual);
    }

    private Subscription testSubscription(long chatId, String url) {
        return Subscription.builder()
            .chatId(chatId)
            .linkUrl(url)
            .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build();
    }

    @Test
    @Transactional
    @Rollback
    void unsubscribe() {
        subscriptionRepository.subscribe(testSubscription(1L, "https://example.com"));

        Subscription actual = subscriptionRepository.unsubscribe(testSubscription(1L, "https://example.com"));

        Subscription expected = testSubscription(1L, "https://example.com");
        assertEquals(expected, actual);
        assertThat(subscriptionRepository.findAllByChatId(1L)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void updateOldByUrl() {
        addTgChat(2L);
        subscriptionRepository.subscribe(
            new Subscription(1L, "https://example.com", OffsetDateTime.parse("2024-03-17T12:00:00Z")));
        subscriptionRepository.subscribe(
            new Subscription(2L, "https://example.com", OffsetDateTime.parse("2024-03-17T14:00:00Z")));

        List<Subscription> actual = subscriptionRepository.updateOldByUrl(
            "https://example.com", OffsetDateTime.parse("2024-03-17T13:00:00Z"));

        List<Subscription> expected = List.of(
            new Subscription(1L, "https://example.com", OffsetDateTime.parse("2024-03-17T13:00:00Z"))
        );
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findByTgChatAndTrackableLink_Found() {
        subscriptionRepository.subscribe(testSubscription(1L, "https://example.com"));

        Optional<Subscription> actual = subscriptionRepository.findByTgChatAndTrackableLink(
            new TgChat(1L),
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        );

        Optional<Subscription> expected = Optional.of(testSubscription(1L, "https://example.com"));
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findByTgChatAndTrackableLink_NotFound() {
        Optional<Subscription> actual = subscriptionRepository.findByTgChatAndTrackableLink(
            new TgChat(1L),
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        );

        Optional<Subscription> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void existsByTgChatAndTrackableLink_ReturnsTrue() {
        subscriptionRepository.subscribe(testSubscription(1L, "https://example.com"));

        boolean exists = subscriptionRepository.existsByTgChatAndTrackableLink(
            new TgChat(1L),
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        );

        assertThat(exists).as("checks that Subscription exists").isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void existsByTgChatAndTrackableLink_ReturnsFalse() {
        boolean exists = subscriptionRepository.existsByTgChatAndTrackableLink(
            new TgChat(1L),
            TrackableLink.builder()
                .url("https://example.com")
                .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
                .build()
        );

        assertThat(exists).as("checks that Subscription does not exist").isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findAllByChatId() {
        addTgChat(2L);
        addTrackableLink("https://example1.com");
        addTrackableLink("https://example2.com");
        addTrackableLink("https://example3.com");

        subscriptionRepository.subscribe(testSubscription(1L, "https://example1.com"));
        subscriptionRepository.subscribe(testSubscription(2L, "https://example2.com"));
        subscriptionRepository.subscribe(testSubscription(1L, "https://example3.com"));

        List<Subscription> actual = subscriptionRepository.findAllByChatId(1L);

        List<Subscription> expected = List.of(
            testSubscription(1L, "https://example1.com"),
            testSubscription(1L, "https://example3.com")
        );
        assertEquals(expected, actual);
    }
}
