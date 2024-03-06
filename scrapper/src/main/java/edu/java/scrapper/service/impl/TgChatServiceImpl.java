package edu.java.scrapper.service.impl;

import edu.java.scrapper.service.TgChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TgChatServiceImpl implements TgChatService {

    @Override
    public ResponseEntity<Void> registerChat(long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> deleteChat(long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
