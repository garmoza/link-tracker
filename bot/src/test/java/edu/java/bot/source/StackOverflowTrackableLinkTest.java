package edu.java.bot.source;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StackOverflowTrackableLinkTest {

    @Test
    void host() {
        TrackableLink trackableLink = new StackOverflowTrackableLink();

        String actualHost = trackableLink.host();

        assertEquals("stackoverflow.com", actualHost);
    }
}
