package edu.java.scrapper.service.impl.jdbc;

import edu.java.model.response.TgChatResponse;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.entity.mapper.TgChatModelMapper;
import edu.java.scrapper.exception.TgChatAlreadyExistsException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import edu.java.scrapper.repository.jdbc.TgChatRepository;
import edu.java.scrapper.service.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final TgChatRepository tgChatRepository;

    @Override
    public ResponseEntity<Void> getChat(long id) {
        if (tgChatRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> registerChat(long id) {
        if (tgChatRepository.existsById(id)) {
            throw new TgChatAlreadyExistsException();
        }
        tgChatRepository.add(new TgChat(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TgChatResponse>> getAllChats() {
        var chats = tgChatRepository.findAll()
            .stream()
            .map(TgChatModelMapper::toTgChatResponse)
            .toList();
        return ResponseEntity.ok(chats);
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
