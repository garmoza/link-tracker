package edu.java.scrapper.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
@ConditionalOnProperty(name = "app.scheduler.enable", havingValue = "true")
public class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    public void update() {
        log.info("check for updates");
    }
}
