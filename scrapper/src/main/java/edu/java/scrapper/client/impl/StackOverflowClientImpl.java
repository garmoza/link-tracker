package edu.java.scrapper.client.impl;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.response.stackoverflow.QuestionResponse;
import reactor.core.publisher.Mono;

public class StackOverflowClientImpl extends AbstractWebClient implements StackOverflowClient {

    public StackOverflowClientImpl() {
        super("https://api.stackexchange.com/2.3");
    }

    public StackOverflowClientImpl(String baseUrl) {
        super(baseUrl);
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
