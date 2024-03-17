package edu.java.scrapper.client;

import edu.java.scrapper.dto.stackoverflow.QuestionResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowClient extends SourceClient {

    Mono<QuestionResponse> fetchQuestion(int id);
}
