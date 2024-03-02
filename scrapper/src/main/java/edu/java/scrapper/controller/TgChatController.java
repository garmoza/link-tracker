package edu.java.scrapper.controller;

import edu.java.scrapper.service.TgChatService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
@Validated
public class TgChatController {

    private final TgChatService tgChatService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> registerChat(@Positive @PathVariable long id) {
        return tgChatService.registerChat(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@Positive @PathVariable long id) {
        return tgChatService.deleteChat(id);
    }
}
