package edu.java.scrapper.client.impl;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.dto.github.RepositoryResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GitHubClientImpl extends AbstractWebClient implements GitHubClient {

    private final String host;

    public GitHubClientImpl(String baseUrl) {
        super(baseUrl);
        host = URI.create(baseUrl).getHost();
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public Mono<RepositoryResponse> fetchRepository(String username, String repo) {
        return webClient.get()
            .uri("/repos/" + username + "/" + repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .onErrorResume(
                WebClientResponseException.class,
                ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.empty() : Mono.error(ex)
            );
    }
}
