package edu.java.scrapper.controller;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.api.LinkControllerApi;
import edu.java.scrapper.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinkController implements LinkControllerApi {

    private final SubscriptionService subscriptionService;

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinks(Long tgChatId) {
        return subscriptionService.getAllLinksByChatId(tgChatId);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(Long tgChatId, AddLinkRequest dto) {
        return subscriptionService.subscribeLink(tgChatId, dto);
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(
        Long tgChatId,
        RemoveLinkRequest dto
    ) {
        return subscriptionService.unsubscribeLink(tgChatId, dto);
    }
}
