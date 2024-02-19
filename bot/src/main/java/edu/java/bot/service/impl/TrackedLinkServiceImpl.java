package edu.java.bot.service.impl;

import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.service.TrackedLinkService;
import edu.java.bot.source.TrackableLink;
import java.util.List;

public class TrackedLinkServiceImpl implements TrackedLinkService {

    private final List<TrackableLink> trackableLinks;

    public TrackedLinkServiceImpl(List<TrackableLink> trackableLinks) {
        this.trackableLinks = trackableLinks;
    }

    @Override
    public boolean isTrackableLink(Link link) {
        for (var trackableLink : trackableLinks) {
            if (trackableLink.supports(link)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean trackLink(User user, Link link) {
        return user.getLinks().add(link);
    }

    @Override
    public boolean untrackLink(User user, Link link) {
        return user.getLinks().remove(link);
    }
}
