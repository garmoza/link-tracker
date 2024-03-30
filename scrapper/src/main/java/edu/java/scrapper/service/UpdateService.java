package edu.java.scrapper.service;

import edu.java.scrapper.entity.TrackableLink;
import java.time.OffsetDateTime;
import java.util.List;

public interface UpdateService {

    List<TrackableLink> getAllLinksNeedToCrawling(OffsetDateTime time);

    void update(TrackableLink link);
}
