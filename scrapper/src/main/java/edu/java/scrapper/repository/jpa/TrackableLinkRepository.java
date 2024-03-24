package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.TrackableLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackableLinkRepository extends CrudRepository<TrackableLink, String> {
}
