package edu.java.scrapper.repository.jpa;

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
        saveTgChat(1L);
        saveTrackableLink("https://example.com");
    }

    private void saveTgChat(long chatId) {
        tgChatRepository.save(new TgChat(chatId));
    }

    private void saveTrackableLink(String url) {
        trackableLinkRepository.save(TrackableLink.builder()
            .url(url)
            .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build());
    }

    @Test
    @Transactional
    @Rollback
    void save() {
        subscriptionRepository.save(testSubscription(1L, "https://example.com"));

        List<Subscription> actual = subscriptionRepository.findAllByChatId(1L);

        List<Subscription> expected = List.of(testSubscription(1L, "https://example.com"));
        assertEquals(expected, actual);
    }

    private Subscription testSubscription(long chatId, String url) {
        return Subscription.builder()
            .id(new Subscription.Id(chatId, url))
            .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build();
    }

    @Test
    @Transactional
    @Rollback
    void delete() {
        subscriptionRepository.save(testSubscription(1L, "https://example.com"));

        subscriptionRepository.delete(testSubscription(1L, "https://example.com"));

        assertThat(subscriptionRepository.findAllByChatId(1L)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAllByUrlAndOlderLastChange() {
        saveTgChat(2L);
        subscriptionRepository.save(
            new Subscription(
                new Subscription.Id(1L, "https://example.com"),
                OffsetDateTime.parse("2024-03-17T12:00:00Z")
            ));
        subscriptionRepository.save(
            new Subscription(
                new Subscription.Id(2L, "https://example.com"),
                OffsetDateTime.parse("2024-03-17T14:00:00Z")
            ));

        List<Subscription> actual = subscriptionRepository.findAllByUrlAndOlderLastChange(
            "https://example.com", OffsetDateTime.parse("2024-03-17T13:00:00Z"));

        List<Subscription> expected = List.of(
            new Subscription(
                new Subscription.Id(1L, "https://example.com"),
                OffsetDateTime.parse("2024-03-17T12:00:00Z")
            )
        );
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findById_Found() {
        subscriptionRepository.save(testSubscription(1L, "https://example.com"));

        Optional<Subscription> actual = subscriptionRepository.findById(new Subscription.Id(
            1L,
            "https://example.com"
        ));

        Optional<Subscription> expected = Optional.of(testSubscription(1L, "https://example.com"));
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findById_NotFound() {
        Optional<Subscription> actual = subscriptionRepository.findById(new Subscription.Id(
            1L,
            "https://example.com"
        ));

        Optional<Subscription> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void existsById_ReturnsTrue() {
        subscriptionRepository.save(testSubscription(1L, "https://example.com"));

        boolean exists = subscriptionRepository.existsById(new Subscription.Id(
            1L,
            "https://example.com"
        ));

        assertThat(exists).as("checks that Subscription exists").isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void existsById_ReturnsFalse() {
        boolean exists = subscriptionRepository.existsById(new Subscription.Id(
            1L,
            "https://example.com"
        ));

        assertThat(exists).as("checks that Subscription does not exist").isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findAllByChatId() {
        saveTgChat(2L);
        saveTrackableLink("https://example1.com");
        saveTrackableLink("https://example2.com");
        saveTrackableLink("https://example3.com");

        subscriptionRepository.save(testSubscription(1L, "https://example1.com"));
        subscriptionRepository.save(testSubscription(2L, "https://example2.com"));
        subscriptionRepository.save(testSubscription(1L, "https://example3.com"));

        List<Subscription> actual = subscriptionRepository.findAllByChatId(1L);

        List<Subscription> expected = List.of(
            testSubscription(1L, "https://example1.com"),
            testSubscription(1L, "https://example3.com")
        );
        assertEquals(expected, actual);
    }
}
