package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.service.ChatService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorizedCommandTest {

    @Test
    void handle_UserNotFound() {
        AuthorizedCommand authorizedCommand = mock(AuthorizedCommand.class, CALLS_REAL_METHODS);
        ChatService chatService = mock(ChatService.class);
        when(chatService.existsById(1L)).thenReturn(false);
        when(authorizedCommand.getChatService()).thenReturn(chatService);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = authorizedCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "User is not registered.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_UserExist() {
        AuthorizedCommand authorizedCommand = mock(AuthorizedCommand.class, CALLS_REAL_METHODS);
        ChatService chatService = mock(ChatService.class);
        when(chatService.existsById(1L)).thenReturn(true);
        when(authorizedCommand.getChatService()).thenReturn(chatService);
        SendMessage expectedMessage = new SendMessage(1L, "Test message");
        when(authorizedCommand.authorizedHandle(any(Update.class), any(Long.class))).thenReturn(expectedMessage);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = authorizedCommand.handle(updateMock);

        verify(authorizedCommand).authorizedHandle(any(Update.class), any(Long.class));
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
