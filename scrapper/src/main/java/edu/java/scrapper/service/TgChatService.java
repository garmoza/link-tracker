package edu.java.scrapper.service;

import org.springframework.http.ResponseEntity;

public interface TgChatService {

    ResponseEntity<Void> registerChat(long id);

    ResponseEntity<Void> deleteChat(long id);
}
