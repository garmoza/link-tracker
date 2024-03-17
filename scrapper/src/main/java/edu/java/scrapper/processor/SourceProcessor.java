package edu.java.scrapper.processor;

import edu.java.scrapper.entity.TrackableLink;
import java.net.URI;

public interface SourceProcessor {

    boolean supports(URI url);

    void processUpdate(TrackableLink trackableLink);
}
