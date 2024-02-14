package edu.java.bot.link;

public class StackOverflowTrackableLink implements TrackableLink {

    @Override
    public String resourceName() {
        return "Stack Overflow";
    }

    @Override
    public String host() {
        return "stackoverflow.com";
    }
}
