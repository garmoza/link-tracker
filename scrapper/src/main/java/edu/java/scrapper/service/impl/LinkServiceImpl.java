package edu.java.scrapper.service.impl;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LinkServiceImpl implements LinkService {

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinksByChatId(long tgChatId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<LinkResponse> subscribeLink(long tgChatId, AddLinkRequest dto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<LinkResponse> unsubscribeLink(long tgChatId, RemoveLinkRequest dto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
