package edu.java.scrapper.repository.jdbc.impl;

import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.repository.jdbc.TrackableLinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTrackableLinkRepository implements TrackableLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public TrackableLink add(TrackableLink link) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO trackable_link VALUES (?, ?, ?) RETURNING *",
            new BeanPropertyRowMapper<>(TrackableLink.class),
            link.getUrl(),
            link.getLastChange(),
            link.getLastCrawl()
        );
    }

    @Override
    public TrackableLink update(TrackableLink link) {
        return jdbcTemplate.queryForObject(
            "UPDATE trackable_link SET last_change=?, last_crawl=? WHERE url=? RETURNING *",
            new BeanPropertyRowMapper<>(TrackableLink.class),
            link.getLastChange(),
            link.getLastCrawl(),
            link.getUrl()
        );
    }

    @Override
    public Optional<TrackableLink> findByUrl(String url) {
        try {
            TrackableLink link = jdbcTemplate.queryForObject(
                "SELECT * FROM trackable_link WHERE url=?",
                new BeanPropertyRowMapper<>(TrackableLink.class),
                url
            );
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUrl(String url) {
        return findByUrl(url).isPresent();
    }

    @Override
    public List<TrackableLink> findAll() {
        return jdbcTemplate.query("SELECT * FROM trackable_link", new BeanPropertyRowMapper<>(TrackableLink.class));
    }

    @Override
    public List<TrackableLink> findAllByLastCrawlOlder(OffsetDateTime time) {
        return jdbcTemplate.query(
            "SELECT * FROM trackable_link WHERE last_crawl < ?",
            new BeanPropertyRowMapper<>(TrackableLink.class),
            time
        );
    }

    @Override
    public void remove(String url) {
        jdbcTemplate.update("DELETE FROM trackable_link WHERE url=?", url);
    }
}
