package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.TrackableLink;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackableLinkRepository extends ListCrudRepository<TrackableLink, String> {

    @Query("SELECT t FROM TrackableLink t WHERE t.lastCrawl < :time")
    List<TrackableLink> findAllByLastCrawlOlder(@Param("time") OffsetDateTime time);
}
