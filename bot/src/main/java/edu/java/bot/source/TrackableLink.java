package edu.java.bot.source;

import edu.java.bot.entity.Link;

public interface TrackableLink {

    String host();

    default boolean supports(Link link) {
        return link.getHost().equals(host());
    }
}
