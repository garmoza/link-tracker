package edu.java.scrapper.processor;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.stackoverflow.QuestionResponse;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.service.UpdateService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StackOverflowProcessor implements SourceProcessor {

    private static final Pattern QUESTION_PATTERN = Pattern.compile("^/questions/(?<questionId>\\d+)(/[\\w-]*)?(/)?$");

    private final StackOverflowClient stackOverflowClient;
    private final UpdateService updateService;

    @Override
    public boolean supports(URI url) {
        return isSupportedHost(url) && isSupportedPath(url);
    }

    private boolean isSupportedHost(URI url) {
        return stackOverflowClient.host().equals(url.getHost());
    }

    private boolean isSupportedPath(URI url) {
        return QUESTION_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public void processUpdate(TrackableLink trackableLink) {
        Matcher matcher = QUESTION_PATTERN.matcher(trackableLink.getUrl());

        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group("questionId"));

            QuestionResponse response = stackOverflowClient.fetchQuestion(id)
                .blockOptional()
                .orElseThrow(RuntimeException::new);

            OffsetDateTime lastChange = response.items().getFirst().lastActivityDate();
            TrackableLink newTrackableLink = TrackableLink.builder()
                .url(trackableLink.getUrl())
                .lastChange(lastChange)
                .lastCrawl(OffsetDateTime.now())
                .build();

            updateService.update(newTrackableLink);
        }
    }
}
