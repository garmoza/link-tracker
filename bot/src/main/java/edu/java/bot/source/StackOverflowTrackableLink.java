package edu.java.bot.source;

import org.springframework.stereotype.Component;

@Component
public class StackOverflowTrackableLink implements TrackableLink {

    @Override
    public String host() {
        return "stackoverflow.com";
    }
}
