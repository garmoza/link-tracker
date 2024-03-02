package edu.java.scrapper.client;

import edu.java.scrapper.dto.request.LinkUpdate;
import reactor.core.publisher.Mono;

public interface BotClient {

    Mono<Void> sendUpdate(LinkUpdate dto);
}
