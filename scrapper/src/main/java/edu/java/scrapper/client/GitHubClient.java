package edu.java.scrapper.client;

import edu.java.scrapper.dto.github.RepositoryResponse;
import reactor.core.publisher.Mono;

public interface GitHubClient extends SourceClient {

    Mono<RepositoryResponse> fetchRepository(String username, String repo);
}
