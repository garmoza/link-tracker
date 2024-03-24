package edu.java.scrapper.service.impl.jpa;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.entity.mapper.SubscriptionModelMapper;
import edu.java.scrapper.exception.LinkAlreadyExistsException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import edu.java.scrapper.repository.jpa.SubscriptionRepository;
import edu.java.scrapper.repository.jpa.TgChatRepository;
import edu.java.scrapper.repository.jpa.TrackableLinkRepository;
import edu.java.scrapper.service.SubscriptionService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaSubscriptionService implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TgChatRepository tgChatRepository;
    private final TrackableLinkRepository trackableLinkRepository;

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinksByChatId(long tgChatId) {
        var links = subscriptionRepository.findAllByChatId(tgChatId);
        var body = SubscriptionModelMapper.toListLinksResponse(links);
        return ResponseEntity.ok(body);
    }

    @Override
    @Transactional
    public ResponseEntity<LinkResponse> subscribeLink(long tgChatId, AddLinkRequest dto) {
        TgChat chat = tgChatRepository.findById(tgChatId)
            .orElseThrow(() -> new TgChatNotFoundException(tgChatId));
        TrackableLink link = trackableLinkRepository.findById(dto.link())
            .orElse(addTrackableLink(dto.link()));

        if (subscriptionRepository.existsById(new Subscription.Id(chat.getId(), link.getUrl()))) {
            LinkResponse response = new LinkResponse(chat.getId(), URI.create(link.getUrl()));
            throw new LinkAlreadyExistsException(response);
        }

        Subscription subscription = subscriptionRepository.save(
            Subscription.builder()
                .id(new Subscription.Id(chat.getId(), link.getUrl()))
                .lastUpdate(link.getLastChange())
                .build()
        );

        var body = SubscriptionModelMapper.toLinkResponse(subscription);

        return ResponseEntity.ok(body);
    }

    private TrackableLink addTrackableLink(String url) {
        var now = OffsetDateTime.now();
        TrackableLink link = TrackableLink.builder()
            .url(url)
            .lastChange(now)
            .lastCrawl(now)
            .build();
        return trackableLinkRepository.save(link);
    }

    @Override
    public ResponseEntity<LinkResponse> unsubscribeLink(long tgChatId, RemoveLinkRequest dto) {
        TgChat chat = tgChatRepository.findById(tgChatId)
            .orElseThrow(() -> new LinkNotFoundException(dto.link()));
        TrackableLink link = trackableLinkRepository.findById(dto.link())
            .orElseThrow(() -> new LinkNotFoundException(dto.link()));
        Subscription sub = subscriptionRepository.findById(new Subscription.Id(chat.getId(), link.getUrl()))
            .orElseThrow(() -> new LinkNotFoundException(dto.link()));

        subscriptionRepository.delete(sub);

        var body = SubscriptionModelMapper.toLinkResponse(sub);

        return ResponseEntity.ok(body);
    }
}
