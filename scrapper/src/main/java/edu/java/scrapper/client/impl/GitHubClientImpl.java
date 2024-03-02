package edu.java.scrapper.client.impl;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.dto.response.github.RepositoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GitHubClientImpl extends AbstractWebClient implements GitHubClient {

    public GitHubClientImpl(String baseUrl) {
        super(baseUrl);
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
