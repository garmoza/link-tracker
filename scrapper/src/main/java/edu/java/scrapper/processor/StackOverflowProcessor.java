package edu.java.scrapper.processor;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.entity.TrackableLink;
import edu.java.scrapper.repository.TrackableLinkRepository;
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
    private final BotClient botClient;
    private final TrackableLinkRepository trackableLinkRepository;

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

            OffsetDateTime lastChange = stackOverflowClient.fetchQuestion(id).blockOptional()
                .map(response -> response.items().getFirst().lastActivityDate())
                .orElseThrow(RuntimeException::new);

            if (lastChange.isAfter(trackableLink.getLastChange())) {
                //TODO: update database, make request with BotClient
            }
        }
    }
}
