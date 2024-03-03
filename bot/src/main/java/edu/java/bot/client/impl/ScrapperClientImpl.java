package edu.java.bot.client.impl;

import edu.java.bot.client.ScrapperClient;
import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class ScrapperClientImpl extends AbstractWebClient implements ScrapperClient {

    private static final String TG_CHAT_ENDPOINT = "/tg-chat/{id}";
    private static final String LINKS_ENDPOINT = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    public ScrapperClientImpl(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public Mono<Void> registerChat(long id) {
        return webClient.post()
            .uri(TG_CHAT_ENDPOINT, id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> deleteChat(long id) {
        return webClient.delete()
            .uri(TG_CHAT_ENDPOINT, id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<ListLinksResponse> getAllLinks(long tgChatId) {
        return webClient.get()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    @Override
    public Mono<LinkResponse> addLink(long tgChatId, AddLinkRequest dto) {
        return webClient.post()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    @Override
    public Mono<LinkResponse> deleteLink(long tgChatId, RemoveLinkRequest dto) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
}
