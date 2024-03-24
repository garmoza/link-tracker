package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.processor.SourceProcessor;
import edu.java.scrapper.repository.jdbc.TrackableLinkRepository;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@ConditionalOnProperty(name = "app.scheduler.enable", havingValue = "true")
public class LinkUpdaterScheduler {

    private final ApplicationConfig appConfig;
    private final TrackableLinkRepository trackableLinkRepository;
    private final List<SourceProcessor> sourceProcessors;

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    public void update() {
        Duration interval = appConfig.scheduler().interval();
        OffsetDateTime time = OffsetDateTime.now().minusSeconds(interval.getSeconds());
        //TODO: replace with configurable service call
        List<TrackableLink> links = trackableLinkRepository.findAllByLastCrawlOlder(time);

        log.info("check for updates");

        for (var trackableLink : links) {
            process(trackableLink);
        }
    }

    private void process(TrackableLink trackableLink) {
        URI url = URI.create(trackableLink.getUrl());
        for (var processor : sourceProcessors) {
            if (processor.supports(url)) {
                processor.processUpdate(trackableLink);
                return;
            }
        }
    }
}
