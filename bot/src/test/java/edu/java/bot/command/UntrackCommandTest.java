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
class UntrackCommandTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TrackedLinkService trackedLinkService;

    @Test
    void command() {
        Command untrackCommand = new UntrackCommand(userRepository, trackedLinkService);

        String actualCommand = untrackCommand.command();

        assertEquals("/untrack", actualCommand);
    }

    @Test
    void description() {
        Command untrackCommand = new UntrackCommand(userRepository, trackedLinkService);

        String actualDescription = untrackCommand.description();

        assertEquals("stop tracking link", actualDescription);
    }

    @Test
    void authorizedHandle_SuccessUntrackLink() {
        when(trackedLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackedLinkService.untrackLink(any(), any())).thenReturn(true);
        UntrackCommand untrackCommand = new UntrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack https://example.com/", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, new User(2L));

        SendMessage expectedMessage = new SendMessage(1L, "Link *https://example.com/* no longer tracked.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_LinkAlreadyNotTracked() {
        when(trackedLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackedLinkService.untrackLink(any(), any())).thenReturn(false);
        UntrackCommand untrackCommand = new UntrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack https://example.com/", 1L);
        User user = new User(2L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, user);

        SendMessage expectedMessage = new SendMessage(1L, "Tracking link not found.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_NoParameters() {
        UntrackCommand untrackCommand = new UntrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, new User(2L));

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */untrack URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_ManyParameters() {
        UntrackCommand untrackCommand = new UntrackCommand(userRepository, trackedLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack link1 link2", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, new User(2L));

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */untrack URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_NotCallAuthorizedHandle_WhenUserNotFound() {
        UntrackCommand untrackCommand = spy(new UntrackCommand(userRepository, trackedLinkService));
        when(userRepository.findUserById(2L)).thenReturn(Optional.empty());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        untrackCommand.handle(updateMock);

        verify(untrackCommand, never()).authorizedHandle(any(Update.class), any(User.class));
    }
}
