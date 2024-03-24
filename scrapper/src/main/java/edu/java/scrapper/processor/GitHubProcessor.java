package edu.java.scrapper.processor;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.dto.github.RepositoryResponse;
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
public class GitHubProcessor implements SourceProcessor {

    private static final Pattern REPOSITORY_PATTERN =
        Pattern.compile("^/(?<username>[a-z-A-Z0-9]+)/(?<repo>[\\w-.]+)(/)?$");

    private final GitHubClient gitHubClient;
    private final UpdateService updateService;

    @Override
    public boolean supports(URI url) {
        return isSupportedHost(url) && isSupportedPath(url);
    }

    private boolean isSupportedHost(URI url) {
        return gitHubClient.host().equals(url.getHost());
    }

    private boolean isSupportedPath(URI url) {
        return REPOSITORY_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public void processUpdate(TrackableLink trackableLink) {
        Matcher matcher = REPOSITORY_PATTERN.matcher(trackableLink.getUrl());

        if (matcher.matches()) {
            String username = matcher.group("username");
            String repo = matcher.group("repo");

            RepositoryResponse response = gitHubClient.fetchRepository(username, repo)
                .blockOptional()
                .orElseThrow(RuntimeException::new);

            OffsetDateTime lastChange = response.pushedAt();
            TrackableLink newTrackableLink = TrackableLink.builder()
                .url(trackableLink.getUrl())
                .lastChange(lastChange)
                .lastCrawl(OffsetDateTime.now())
                .build();

            updateService.update(newTrackableLink);
        }
    }
}
