package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.entity.TrackableLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrackableLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private TrackableLinkRepository trackableLinkRepository;

    @Test
    @Transactional
    @Rollback
    void add() {
        trackableLinkRepository.add(testTrackableLink("https://example.com"));

        List<TrackableLink> actual = trackableLinkRepository.findAll();

        List<TrackableLink> expected = List.of(testTrackableLink("https://example.com"));
        assertEquals(expected, actual);
    }

    private TrackableLink testTrackableLink(String url) {
        return TrackableLink.builder()
            .url(url)
            .lastChange(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .lastCrawl(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build();
    }

    @Test
    @Transactional
    @Rollback
    void findByUrl_Found() {
        trackableLinkRepository.add(testTrackableLink("https://example.com"));

        Optional<TrackableLink> actual = trackableLinkRepository.findByUrl("https://example.com");

        Optional<TrackableLink> expected = Optional.of(testTrackableLink("https://example.com"));
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findByUrl_NotFound() {
        Optional<TrackableLink> actual = trackableLinkRepository.findByUrl("https://example.com");

        assertThat(actual).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void existsByUrl_ReturnsTrue() {
        trackableLinkRepository.add(testTrackableLink("https://example.com"));

        boolean exists = trackableLinkRepository.existsByUrl("https://example.com");

        assertThat(exists).as("checks that TrackableLink exists").isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void existsByUrl_ReturnsFalse() {
        boolean exists = trackableLinkRepository.existsByUrl("https://example.com");

        assertThat(exists).as("checks that TrackableLink does not exist").isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        trackableLinkRepository.add(testTrackableLink("https://example1.com"));
        trackableLinkRepository.add(testTrackableLink("https://example2.com"));

        List<TrackableLink> actual = trackableLinkRepository.findAll();

        List<TrackableLink> expected = List.of(
            testTrackableLink("https://example1.com"),
            testTrackableLink("https://example2.com")
        );
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        trackableLinkRepository.add(testTrackableLink("https://example.com"));

        trackableLinkRepository.remove("https://example.com");

        boolean isDeleted = !trackableLinkRepository.existsByUrl("https://example.com");
        assertThat(isDeleted).as("checks that TrackableLink has been deleted").isTrue();
    }
}
