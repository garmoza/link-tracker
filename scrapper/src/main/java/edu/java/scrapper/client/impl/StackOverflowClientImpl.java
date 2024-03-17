package edu.java.scrapper.client.impl;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.stackoverflow.QuestionResponse;
import java.net.URI;
import reactor.core.publisher.Mono;

public class StackOverflowClientImpl extends AbstractWebClient implements StackOverflowClient {

    private final String host;

    public StackOverflowClientImpl(String baseUrl) {
        super(baseUrl);
        host = URI.create(baseUrl).getHost();
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public Mono<QuestionResponse> fetchQuestion(int id) {
        return webClient.get()
            .uri("/questions/" + id + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .filter(response -> response.items() != null && !response.items().isEmpty());
    }
}
