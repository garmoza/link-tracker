package edu.java.bot.source;

import org.springframework.stereotype.Component;

@Component
public class GitHubTrackableLink implements TrackableLink {

    @Override
    public String host() {
        return "github.com";
    }
}
