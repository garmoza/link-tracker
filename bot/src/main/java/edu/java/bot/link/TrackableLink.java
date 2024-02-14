package edu.java.bot.link;

import edu.java.bot.entity.Link;

public interface TrackableLink {

    String resourceName();

    String host();

    default boolean supports(Link link) {
        return link.getHost().equals(host());
    }
}
