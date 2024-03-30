package edu.java.bot.service;

import java.util.List;

public interface TrackableLinkService {

    List<String> getTrackableLinks(long chatId);

    boolean isTrackableLink(String link);

    boolean subscribe(long chatId, String link);

    boolean unsubscribe(long chatId, String link);
}
