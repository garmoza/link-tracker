package edu.java.scrapper.service;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;

public interface LinkService {

    ResponseEntity<ListLinksResponse> getAllLinks(long tgChatId);

    ResponseEntity<LinkResponse> addLink(long tgChatId, AddLinkRequest dto);

    ResponseEntity<LinkResponse> deleteLink(long tgChatId, RemoveLinkRequest dto);
}
