package edu.java.bot.service;

import edu.java.bot.entity.User;

public interface TrackedLinkService {

    boolean trackLink(User user, String url);

    boolean untrackLink(User user, String url);

    boolean isTrackedNow(User user, String url);
}
