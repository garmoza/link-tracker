package edu.java.scrapper.service;

import edu.java.model.response.TgChatResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface TgChatService {

    ResponseEntity<Void> registerChat(long id);

    ResponseEntity<List<TgChatResponse>> getAllChats();

    ResponseEntity<Void> deleteChat(long id);
}
