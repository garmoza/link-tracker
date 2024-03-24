package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.Subscription;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Subscription.Id> {

    @Query("SELECT s FROM Subscription s WHERE s.id.chatId = :chatId")
    List<Subscription> findAllByChatId(@Param("chatId") long chatId);

    @Query("UPDATE Subscription SET lastUpdate = :lastChange WHERE id.linkUrl = :url AND lastUpdate < :lastChange")
    List<Subscription> updateOldByUrl(@Param("url") String url, @Param("lastChange") OffsetDateTime lastChange);
}
