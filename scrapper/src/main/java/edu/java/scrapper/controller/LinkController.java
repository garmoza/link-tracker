package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks() {
        return linkService.getAllLinks();
    }

    @PostMapping
    public ResponseEntity<Void> addLink(AddLinkRequest dto) {
        return linkService.addLink(dto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLink(RemoveLinkRequest dto) {
        return linkService.deleteLink(dto);
    }
}
