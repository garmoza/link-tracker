package edu.java.scrapper.client.impl;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.dto.github.RepositoryResponse;
import reactor.core.publisher.Mono;

public class GitHubClientImpl extends AbstractWebClient implements GitHubClient {

    public GitHubClientImpl() {
        super("https://api.github.com");
    }

    public GitHubClientImpl(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public Mono<RepositoryResponse> fetchRepository(String username, String repo) {
        return webClient.get()
            .uri("/repos/" + username + "/" + repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class);
    }
}