package edu.java.scrapper.service;

import edu.java.model.request.LinkUpdate;
import java.time.OffsetDateTime;

public interface UpdateService {

    void update(LinkUpdate linkUpdate, OffsetDateTime lastChange, OffsetDateTime lastCrawl);
}
