package edu.java.bot.source;

import edu.java.bot.entity.Link;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackableLinkTest {

    @Spy
    private TrackableLink trackableLink;

    @Test
    void supports_ReturnsTrue() {
        when(trackableLink.host()).thenReturn("host");

        boolean isSupports = trackableLink.supports(new Link("url", "host"));

        assertThat(isSupports).as("checks that source (host) is supported").isTrue();
    }

    @Test
    void support_ReturnsFalse() {
        when(trackableLink.host()).thenReturn("another host");

        boolean isSupports = trackableLink.supports(new Link("url", "host"));

        assertThat(isSupports).as("checks that source (host) is not supported");
    }
}
