package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.TrackedLinkService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackCommandTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TrackedLinkService trackedLinkService;

    @Test
    void command() {
        CommandHandler trackCommand = new TrackCommand(userRepository, trackedLinkService);

        String actualCommand = trackCommand.command();

        assertEquals("/track", actualCommand);
    }

    @Test
    void description() {
        CommandHandler trackCommand = new TrackCommand(userRepository, trackedLinkService);

        String actualDescription = trackCommand.description();

        assertEquals("start tracking link", actualDescription);
    }

    @Test
    void authorizedHandle_SuccessAddingLink() {
        when(trackedLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackedLinkService.trackLink(any(), any())).thenReturn(true);
        TrackCommand trackCommand = new TrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track https://example.com/", 1L);
        User user = new User(2L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, user);

        SendMessage expectedMessage = new SendMessage(1L, "Link *https://example.com/* successfully added.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_LinkAlreadyTracking() {
        when(trackedLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackedLinkService.trackLink(any(), any())).thenReturn(false);
        TrackCommand trackCommand = new TrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track https://example.com/", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, new User(2L));

        SendMessage expectedMessage = new SendMessage(1L, "Link already tracked.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_NoParameters() {
        TrackCommand trackCommand = new TrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, new User(2L));

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */track URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_ManyParameters() {
        TrackCommand trackCommand = new TrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track link1 link2", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, new User(2L));

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */track URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_NotCallAuthorizedHandle_WhenUserNotFound() {
        TrackCommand trackCommand = spy(new TrackCommand(userRepository, trackedLinkService));
        when(userRepository.findUserById(2L)).thenReturn(Optional.empty());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        trackCommand.handle(updateMock);

        verify(trackCommand, never()).authorizedHandle(any(Update.class), any(User.class));
    }
}
