package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.service.UserService;
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
    void handle_UserNotFound() {
        AuthorizedCommand authorizedCommand = mock(AuthorizedCommand.class, CALLS_REAL_METHODS);
        UserService userService = mock(UserService.class);
        when(userService.findUserById(2L)).thenReturn(Optional.empty());
        when(authorizedCommand.getUserService()).thenReturn(userService);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessage actualMessage = authorizedCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "User is not registered.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_UserExist() {
        AuthorizedCommand authorizedCommand = mock(AuthorizedCommand.class, CALLS_REAL_METHODS);
        UserService userService = mock(UserService.class);
        when(userService.findUserById(2L)).thenReturn(Optional.of(new User(2L)));
        when(authorizedCommand.getUserService()).thenReturn(userService);
        SendMessage expectedMessage = new SendMessage(1L, "Test message");
        when(authorizedCommand.authorizedHandle(any(Update.class), any(User.class))).thenReturn(expectedMessage);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessage actualMessage = authorizedCommand.handle(updateMock);

        verify(authorizedCommand).authorizedHandle(any(Update.class), any(User.class));
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
