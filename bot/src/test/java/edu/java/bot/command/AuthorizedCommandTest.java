package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorizedCommandTest {

    @Test
    void handleUserNotFound() {
        AuthorizedCommand authorizedCommand = mock(AuthorizedCommand.class, CALLS_REAL_METHODS);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUserById(2L)).thenReturn(Optional.empty());
        when(authorizedCommand.getUserRepository()).thenReturn(userRepository);

        Update updateMock = CommandMocks.getUpdateMock(1L, 2L);
        SendMessage actualMessage = authorizedCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "User is not registered.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handleUserExist() {
        AuthorizedCommand authorizedCommand = mock(AuthorizedCommand.class, CALLS_REAL_METHODS);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUserById(2L)).thenReturn(Optional.of(new User(2L)));
        when(authorizedCommand.getUserRepository()).thenReturn(userRepository);
        SendMessage expectedMessage = new SendMessage(1L, "Test message");
        when(authorizedCommand.authorizedHandle(any(Update.class), any(User.class))).thenReturn(expectedMessage);

        Update updateMock = CommandMocks.getUpdateMock(1L, 2L);
        SendMessage actualMessage = authorizedCommand.handle(updateMock);

        verify(authorizedCommand).authorizedHandle(any(Update.class), any(User.class));
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
