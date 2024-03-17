package edu.java.scrapper.repository;

import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.TrackableLink;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {

    Subscription subscribe(Subscription subscription);

    Subscription unsubscribe(Subscription subscription);

    Subscription update(Subscription subscription);

    Optional<Subscription> findByTgChatAndTrackableLink(TgChat chat, TrackableLink link);

    boolean existsByTgChatAndTrackableLink(TgChat chat, TrackableLink link);

    List<Subscription> findAllByChatId(long chatId);
}
