package edu.java.scrapper.repository;

import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.TrackableLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {

    Subscription subscribe(Subscription subscription);

    Subscription unsubscribe(Subscription subscription);

    List<Subscription> updateOldByUrl(String url, OffsetDateTime lastChange);

    Optional<Subscription> findByTgChatAndTrackableLink(TgChat chat, TrackableLink link);

    boolean existsByTgChatAndTrackableLink(TgChat chat, TrackableLink link);

    List<Subscription> findAllByChatId(long chatId);
}
