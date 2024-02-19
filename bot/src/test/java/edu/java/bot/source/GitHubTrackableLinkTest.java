package edu.java.bot.source;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GitHubTrackableLinkTest {

    @Test
    void host() {
        TrackableLink trackableLink = new GitHubTrackableLink();

        String actualHost = trackableLink.host();

        assertEquals("github.com", actualHost);
    }
}
