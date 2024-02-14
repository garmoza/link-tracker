package edu.java.bot.link;

public class GitHubTrackableLink implements TrackableLink {

    @Override
    public String resourceName() {
        return "GitHub";
    }

    @Override
    public String host() {
        return "github.com";
    }
}
