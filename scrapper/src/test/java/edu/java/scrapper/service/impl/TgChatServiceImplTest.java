package edu.java.scrapper.service.impl;

import edu.java.model.response.TgChatResponse;
import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.exception.TgChatAlreadyExistsException;
import edu.java.scrapper.exception.TgChatNotFoundException;
import edu.java.scrapper.repository.TgChatRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TgChatServiceImplTest {

    @Mock
    private TgChatRepository tgChatRepository;
    @InjectMocks
    private TgChatServiceImpl tgChatService;

    @Test
    void registerChat_NewChat() {
        when(tgChatRepository.existsById(1L)).thenReturn(false);
        when(tgChatRepository.add(any())).thenReturn(new TgChat(1L));

        var actual = tgChatService.registerChat(1L);

        var expected = new ResponseEntity<>(HttpStatus.OK);
        assertEquals(expected, actual);
    }

    @Test
    void registerChat_AlreadyExists() {
        when(tgChatRepository.existsById(1L)).thenReturn(true);

        assertThrows(TgChatAlreadyExistsException.class, () -> tgChatService.registerChat(1L));
    }

    @Test
    void getAllChats() {
        when(tgChatRepository.findAll()).thenReturn(List.of(new TgChat(1L)));

        var actual = tgChatService.getAllChats();

        var expected = ResponseEntity.ok(List.of(new TgChatResponse(1L)));
        assertEquals(actual, expected);
    }

    @Test
    void deleteChat_ChatExists() {
        when(tgChatRepository.existsById(1L)).thenReturn(true);

        tgChatService.deleteChat(1L);

        verify(tgChatRepository).remove(1L);
    }

    @Test
    void deleteChat_ChatNotExists() {
        when(tgChatRepository.existsById(1L)).thenReturn(false);

        assertThrows(TgChatNotFoundException.class, () -> tgChatService.deleteChat(1L));
    }
}
