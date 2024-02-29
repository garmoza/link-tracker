package edu.java.scrapper.controller;

import edu.java.scrapper.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TgChatController {

    private final TgChatService tgChatService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> registerChat(@PathVariable long id) {
        return tgChatService.registerChat(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable long id) {
        return tgChatService.deleteChat(id);
    }
}
