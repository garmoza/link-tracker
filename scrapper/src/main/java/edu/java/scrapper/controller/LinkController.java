package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") long tgChatId) {
        return linkService.getAllLinks(tgChatId);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody AddLinkRequest dto
    ) {
        return linkService.addLink(tgChatId, dto);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody RemoveLinkRequest dto
    ) {
        return linkService.deleteLink(tgChatId, dto);
    }
}
