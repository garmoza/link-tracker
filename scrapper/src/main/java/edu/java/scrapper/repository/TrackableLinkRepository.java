package edu.java.scrapper.repository;

import edu.java.scrapper.entity.TrackableLink;
import java.util.List;
import java.util.Optional;

public interface TrackableLinkRepository {

    TrackableLink add(TrackableLink link);

    Optional<TrackableLink> findByUrl(String url);

    boolean existsByUrl(String url);

    List<TrackableLink> findAll();

    void remove(String url);
}
