package edu.java.bot.service;

import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;

public class TrackedLinkServiceImpl implements TrackedLinkService {

    @Override
    public boolean trackLink(User user, Link url) {
        return user.getLinks().add(url);
    }

    @Override
    public boolean untrackLink(User user, Link url) {
        return user.getLinks().remove(url);
    }
}
