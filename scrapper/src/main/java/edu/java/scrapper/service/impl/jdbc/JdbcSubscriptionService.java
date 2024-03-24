package edu.java.scrapper.service.impl.jdbc;

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
import edu.java.scrapper.repository.jdbc.SubscriptionRepository;
import edu.java.scrapper.repository.jdbc.TgChatRepository;
import edu.java.scrapper.repository.jdbc.TrackableLinkRepository;
import edu.java.scrapper.service.SubscriptionService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcSubscriptionService implements SubscriptionService {

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
        TrackableLink link = trackableLinkRepository.findByUrl(dto.link())
            .orElse(addTrackableLink(dto.link()));

        if (subscriptionRepository.existsByTgChatAndTrackableLink(chat, link)) {
            LinkResponse response = new LinkResponse(chat.getId(), URI.create(link.getUrl()));
            throw new LinkAlreadyExistsException(response);
        }

        Subscription subscription = subscriptionRepository.subscribe(
            Subscription.builder()
                .id(new Subscription.Id(chat.getId(), link.getUrl()))
                .lastUpdate(link.getLastChange())
                .build()
        );

        var body = SubscriptionModelMapper.toLinkResponse(subscription);

        return ResponseEntity.ok(body);
    }

    private TrackableLink addTrackableLink(String url) {
        TrackableLink link = TrackableLink.builder()
            .url(url)
            .lastChange(OffsetDateTime.now())
            .lastCrawl(OffsetDateTime.now())
            .build();
        return trackableLinkRepository.add(link);
    }

    @Override
    public ResponseEntity<LinkResponse> unsubscribeLink(long tgChatId, RemoveLinkRequest dto) {
        TgChat chat = tgChatRepository.findById(tgChatId)
            .orElseThrow(() -> new LinkNotFoundException(dto.link()));
        TrackableLink link = trackableLinkRepository.findByUrl(dto.link())
            .orElseThrow(() -> new LinkNotFoundException(dto.link()));
        Subscription sub = subscriptionRepository.findByTgChatAndTrackableLink(chat, link)
            .orElseThrow(() -> new LinkNotFoundException(dto.link()));

        sub = subscriptionRepository.unsubscribe(sub);

        var body = SubscriptionModelMapper.toLinkResponse(sub);

        return ResponseEntity.ok(body);
    }
}
