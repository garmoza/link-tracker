package edu.java.bot.service;

import edu.java.bot.entity.User;

public class TrackedLinkServiceImpl implements TrackedLinkService {

    @Override
    public boolean trackLink(User user, String url) {
        return user.getLinks().add(url);
    }

    @Override
    public boolean untrackLink(User user, String url) {
        return user.getLinks().remove(url);
    }

    @Override
    public boolean isTrackedNow(User user, String url) {
        return false;
    }
}
