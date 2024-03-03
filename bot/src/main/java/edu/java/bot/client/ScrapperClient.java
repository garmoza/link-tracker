package edu.java.bot.client;

import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import reactor.core.publisher.Mono;

public interface ScrapperClient {

    Mono<Void> registerChat(long id);

    Mono<Void> deleteChat(long id);

    Mono<ListLinksResponse> getAllLinks(long tgChatId);

    Mono<LinkResponse> addLink(long tgChatId, AddLinkRequest dto);

    Mono<LinkResponse> deleteLink(long tgChatId, RemoveLinkRequest dto);
}