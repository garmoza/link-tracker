package edu.java.scrapper.service.impl;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LinkServiceImpl implements LinkService {

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinks(long tgChatId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(long tgChatId, AddLinkRequest dto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(long tgChatId, RemoveLinkRequest dto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
