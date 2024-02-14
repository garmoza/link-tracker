package edu.java.bot.service;

import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;

public interface TrackedLinkService {

    boolean isTrackableLink(Link link);

    boolean trackLink(User user, Link link);

    boolean untrackLink(User user, Link link);
}
