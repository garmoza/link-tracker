package edu.java.scrapper.controller;

import edu.java.model.response.TgChatResponse;
import edu.java.scrapper.api.TgChatControllerApi;
import edu.java.scrapper.service.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TgChatController implements TgChatControllerApi {

    private final TgChatService tgChatService;

    @Override
    public ResponseEntity<Void> registerChat(Long id) {
        return tgChatService.registerChat(id);
    }

    @Override
    public ResponseEntity<List<TgChatResponse>> getAllChats() {
        return tgChatService.getAllChats();
    }

    @Override
    public ResponseEntity<Void> deleteChat(Long id) {
        return tgChatService.deleteChat(id);
    }
}
