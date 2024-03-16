package edu.java.scrapper.service.impl;

import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.exception.TgChatAlreadyExistsException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import edu.java.scrapper.repository.TgChatRepository;
import edu.java.scrapper.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TgChatServiceImpl implements TgChatService {

    private final TgChatRepository tgChatRepository;

    @Override
    public ResponseEntity<Void> registerChat(long id) {
        if (tgChatRepository.existsById(id)) {
            throw new TgChatAlreadyExistsException();
        }
        tgChatRepository.add(new TgChat(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteChat(long id) {
        if (!tgChatRepository.existsById(id)) {
            throw new TgChatNotFoundException(id);
        }
        tgChatRepository.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
