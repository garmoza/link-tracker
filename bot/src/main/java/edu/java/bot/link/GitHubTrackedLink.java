package edu.java.bot.link;

public class GitHubTrackedLink implements TrackedLink {

    @Override
    public String resourceName() {
        return "GitHub";
    }

    @Override
    public String host() {
        return "github.com";
    }
}
