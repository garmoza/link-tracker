package edu.java.scrapper.service;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;

public interface LinkService {

    ResponseEntity<ListLinksResponse> getAllLinks(long tgChatId);

    ResponseEntity<LinkResponse> addLink(long tgChatId, AddLinkRequest dto);

    ResponseEntity<LinkResponse> deleteLink(long tgChatId, RemoveLinkRequest dto);
}
