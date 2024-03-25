package edu.java.bot.service.impl;

import edu.java.bot.service.TrackableLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackableLinkServiceImpl implements TrackableLinkService {

    @Override
    public List<String> getTrackableLinks(long chatId) {
        return null;
    }

    @Override
    public boolean isTrackableLink(String link) {
        return false;
    }

    @Override
    public boolean subscribe(long chatId, String link) {
        return false;
    }

    @Override
    public boolean unsubscribe(long chatId, String link) {
        return false;
    }
}
