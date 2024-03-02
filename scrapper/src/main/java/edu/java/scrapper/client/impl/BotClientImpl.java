package edu.java.scrapper.client.impl;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.dto.request.LinkUpdate;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class BotClientImpl extends AbstractWebClient implements BotClient {

    public BotClientImpl(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public Mono<Void> sendUpdate(LinkUpdate dto) {
        return webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(Void.class);
    }
}
