package edu.java.bot.link;

public class StackOverflowTrackedLink implements TrackedLink {

    @Override
    public String resourceName() {
        return "Stack Overflow";
    }

    @Override
    public String host() {
        return "stackoverflow.com";
    }
}
