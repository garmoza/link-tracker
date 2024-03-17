package edu.java.scrapper.service.impl;

import edu.java.model.request.LinkUpdate;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.repository.TrackableLinkRepository;
import edu.java.scrapper.service.UpdateService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcUpdateService implements UpdateService {

    private final TrackableLinkRepository trackableLinkRepository;

    @Override
    @Transactional
    public void update(LinkUpdate linkUpdate, OffsetDateTime lastChange, OffsetDateTime lastCrawl) {
        TrackableLink trackableLink = TrackableLink.builder()
            .url(linkUpdate.url())
            .lastChange(lastChange)
            .lastCrawl(lastCrawl)
            .build();
        trackableLinkRepository.update(trackableLink);

        //TODO: update subscriptions by url, add ids to linkUpdate, request BotClient
    }
}
