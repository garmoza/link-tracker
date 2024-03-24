package edu.java.scrapper.repository;

import edu.java.scrapper.entity.TrackableLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackableLinkRepository {

    TrackableLink add(TrackableLink link);

    TrackableLink update(TrackableLink link);

    Optional<TrackableLink> findByUrl(String url);

    boolean existsByUrl(String url);

    List<TrackableLink> findAll();

    List<TrackableLink> findAllByLastCrawlOlder(OffsetDateTime time);

    void remove(String url);
}
