package edu.java.bot.service.impl;

import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.source.TrackableLink;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrackedLinkServiceImplTest {

    @Test
    void isTrackableLink_ReturnsTrue() {
        TrackableLink trackableLink = mock(TrackableLink.class);
        when(trackableLink.supports(any(Link.class))).thenReturn(true);

        TrackedLinkServiceImpl trackedLinkService = new TrackedLinkServiceImpl(List.of(trackableLink));
        boolean isTrackable = trackedLinkService.isTrackableLink(new Link("url", "host"));

        assertThat(isTrackable).as("checks that link is trackable").isTrue();
    }

    @Test
    void isTrackableLink_ReturnsFalse() {
        TrackableLink trackableLink = mock(TrackableLink.class);
        when(trackableLink.supports(any(Link.class))).thenReturn(false);

        TrackedLinkServiceImpl trackedLinkService = new TrackedLinkServiceImpl(List.of(trackableLink));
        boolean isTrackable = trackedLinkService.isTrackableLink(new Link("url", "host"));

        assertThat(isTrackable).as("checks that link is not trackable").isFalse();
    }

    @Test
    void trackLink_NewLinkAdded() {
        User user = new User(1L);
        TrackedLinkServiceImpl trackedLinkService = new TrackedLinkServiceImpl(List.of());

        boolean newLinkAdded = trackedLinkService.trackLink(user, new Link("url", "host"));

        assertThat(newLinkAdded).as("checks that new link is added").isTrue();
        assertThat(user.getLinks()).hasSize(1);
    }

    @Test
    void trackLink_LinkAlreadyExists() {
        User user = new User(1L);
        user.getLinks().add(new Link("url", "host"));
        TrackedLinkServiceImpl trackedLinkService = new TrackedLinkServiceImpl(List.of());

        boolean newLinkAdded = trackedLinkService.trackLink(user, new Link("url", "host"));

        assertThat(newLinkAdded).as("checks that link already exists").isFalse();
        assertThat(user.getLinks()).hasSize(1);
    }

    @Test
    void untrackLink_LinkExists() {
        User user = new User(1L);
        user.getLinks().add(new Link("url", "host"));
        TrackedLinkServiceImpl trackedLinkService = new TrackedLinkServiceImpl(List.of());

        boolean linkRemoved = trackedLinkService.untrackLink(user, new Link("url", "host"));

        assertThat(linkRemoved).as("checks that link has been removed").isTrue();
        assertThat(user.getLinks()).isEmpty();
    }

    @Test
    void untrackLink_LinkAlreadyNotExists() {
        User user = new User(1L);
        TrackedLinkServiceImpl trackedLinkService = new TrackedLinkServiceImpl(List.of());

        boolean linkRemoved = trackedLinkService.untrackLink(user, new Link("url", "host"));

        assertThat(linkRemoved).as("checks that link was missing before (not removed)").isFalse();
    }
}
