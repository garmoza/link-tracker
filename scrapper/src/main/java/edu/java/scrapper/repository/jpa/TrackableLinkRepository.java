package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.TrackableLink;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackableLinkRepository extends ListCrudRepository<TrackableLink, String> {
}
