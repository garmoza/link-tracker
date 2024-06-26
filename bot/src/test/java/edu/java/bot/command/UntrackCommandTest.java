package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.service.ChatService;
import edu.java.bot.service.TrackableLinkService;
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
    private ChatService chatService;
    @Mock
    private TrackableLinkService trackableLinkService;

    @Test
    void command() {
        CommandHandler untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        String actualCommand = untrackCommand.command();

        assertEquals("/untrack", actualCommand);
    }

    @Test
    void description() {
        CommandHandler untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        String actualDescription = untrackCommand.description();

        assertEquals("stop tracking link", actualDescription);
    }

    @Test
    void authorizedHandle_SuccessUntrackLink() {
        when(trackableLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackableLinkService.unsubscribe(1L, "https://example.com/")).thenReturn(true);
        UntrackCommand untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack https://example.com/", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Link *https://example.com/* no longer tracked.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_LinkAlreadyNotTracked() {
        when(trackableLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackableLinkService.unsubscribe(1L, "https://example.com/")).thenReturn(false);
        UntrackCommand untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack https://example.com/", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Tracking link not found.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_NoParameters() {
        UntrackCommand untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */untrack URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_ManyParameters() {
        UntrackCommand untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/untrack link1 link2", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */untrack URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_NotValidURL() {
        UntrackCommand untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track not-valid-URL", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Impossible to parse URL.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_ResourceNotSupported() {
        when(trackableLinkService.isTrackableLink(any())).thenReturn(false);
        UntrackCommand untrackCommand = new UntrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track https://example.com/", 1L);
        SendMessage actualMessage = untrackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Resource not supported.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_NotCallAuthorizedHandle_WhenUserNotFound() {
        UntrackCommand untrackCommand = spy(new UntrackCommand(chatService, trackableLinkService));
        when(chatService.existsById(1L)).thenReturn(false);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        untrackCommand.handle(updateMock);

        verify(untrackCommand, never()).authorizedHandle(any(Update.class), any(Long.class));
    }
}
