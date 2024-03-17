package edu.java.scrapper.service;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;

public interface SubscriptionService {

    ResponseEntity<ListLinksResponse> getAllLinksByChatId(long tgChatId);

    ResponseEntity<LinkResponse> subscribeLink(long tgChatId, AddLinkRequest dto);

    ResponseEntity<LinkResponse> unsubscribeLink(long tgChatId, RemoveLinkRequest dto);
}
