package edu.java.scrapper.service;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;

public interface LinkService {

    ResponseEntity<ListLinksResponse> getAllLinks();

    ResponseEntity<Void> addLink(AddLinkRequest dto);

    ResponseEntity<Void> deleteLink(RemoveLinkRequest dto);
}
