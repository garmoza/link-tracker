package edu.java.scrapper.repository.jdbc.impl;

import edu.java.scrapper.entity.Subscription;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.repository.jdbc.SubscriptionRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements SubscriptionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Subscription subscribe(Subscription subscription) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO subscribe VALUES (?, ?, ?) RETURNING *",
            this::rowMapper,
            subscription.getId().getChatId(),
            subscription.getId().getLinkUrl(),
            subscription.getLastUpdate()
        );
    }

    private Subscription rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return Subscription.builder()
            .id(Subscription.Id.builder()
                .chatId(rs.getLong("tg_chat_id"))
                .linkUrl(rs.getString("trackable_link_url"))
                .build())
            .lastUpdate(rs.getObject("last_update", OffsetDateTime.class))
            .build();
    }

    @Override
    public Subscription unsubscribe(Subscription subscription) {
        return jdbcTemplate.queryForObject(
            "DELETE FROM subscribe WHERE tg_chat_id=? AND trackable_link_url=? RETURNING *",
            this::rowMapper,
            subscription.getId().getChatId(),
            subscription.getId().getLinkUrl()
        );
    }

    @Override
    public List<Subscription> updateOldByUrl(String url, OffsetDateTime lastChange) {
        return jdbcTemplate.query(
            "UPDATE subscribe SET last_update=? WHERE trackable_link_url=? AND last_update < ? RETURNING *",
            this::rowMapper,
            lastChange,
            url,
            lastChange
        );
    }

    @Override
    public Optional<Subscription> findByTgChatAndTrackableLink(TgChat chat, TrackableLink link) {
        try {
            Subscription subscription = jdbcTemplate.queryForObject(
                "SELECT * FROM subscribe WHERE tg_chat_id=? AND trackable_link_url=?",
                this::rowMapper,
                chat.getId(),
                link.getUrl()
            );
            return Optional.ofNullable(subscription);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByTgChatAndTrackableLink(TgChat chat, TrackableLink link) {
        return findByTgChatAndTrackableLink(chat, link).isPresent();
    }

    @Override
    public List<Subscription> findAllByChatId(long chatId) {
        return jdbcTemplate.query(
            "SELECT * FROM subscribe WHERE tg_chat_id=?",
            this::rowMapper,
            chatId
        );
    }
}
