package edu.java.scrapper.service.impl;

import edu.java.model.request.LinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.repository.SubscriptionRepository;
import edu.java.scrapper.repository.TrackableLinkRepository;
import edu.java.scrapper.service.UpdateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcUpdateService implements UpdateService {

    private final TrackableLinkRepository trackableLinkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BotClient botClient;

    @Override
    @Transactional
    public void update(TrackableLink link) {
        trackableLinkRepository.update(link);
        List<Subscription> updatedSubs = subscriptionRepository.updateOldByUrl(link.getUrl(), link.getLastChange());

        List<Long> tgChatIds = updatedSubs.stream()
            .map(sub -> sub.getId().getChatId())
            .toList();
        LinkUpdate linkUpdate = LinkUpdate.builder()
            .url(link.getUrl())
            .description("Resource has been changed")
            .tgChatIds(tgChatIds)
            .build();
        botClient.sendUpdate(linkUpdate).block();
    }
}
