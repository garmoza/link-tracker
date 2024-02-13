package edu.java.bot.link;

import java.net.URL;

public interface TrackedLink {

    String resourceName();

    String host();

    default boolean supports(URL link) {
        return link.getHost().equals(host());
    }
}
