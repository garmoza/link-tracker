package edu.java.scrapper.client;

import edu.java.scrapper.dto.response.stackoverflow.QuestionResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {

    Mono<QuestionResponse> fetchQuestion(int id);
}
