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
class TrackCommandTest {

    @Mock
    private ChatService chatService;
    @Mock
    private TrackableLinkService trackableLinkService;

    @Test
    void command() {
        CommandHandler trackCommand = new TrackCommand(chatService, trackableLinkService);

        String actualCommand = trackCommand.command();

        assertEquals("/track", actualCommand);
    }

    @Test
    void description() {
        CommandHandler trackCommand = new TrackCommand(chatService, trackableLinkService);

        String actualDescription = trackCommand.description();

        assertEquals("start tracking link", actualDescription);
    }

    @Test
    void authorizedHandle_SuccessAddingLink() {
        when(trackableLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackableLinkService.subscribe(1L, "https://example.com/")).thenReturn(true);
        TrackCommand trackCommand = new TrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track https://example.com/", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Link *https://example.com/* successfully added.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_LinkAlreadyTracking() {
        when(trackableLinkService.isTrackableLink(any())).thenReturn(true);
        when(trackableLinkService.subscribe(1L, "https://example.com/")).thenReturn(false);
        TrackCommand trackCommand = new TrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track https://example.com/", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Link already tracked.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_NoParameters() {
        TrackCommand trackCommand = new TrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */track URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_WrongFormat_ManyParameters() {
        TrackCommand trackCommand = new TrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track link1 link2", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Pls, enter the command in */track URL* format.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_NotValidURL() {
        TrackCommand trackCommand = new TrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track not-valid-URL", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Impossible to parse URL.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void authorizedHandle_ResourceNotSupported() {
        when(trackableLinkService.isTrackableLink(any())).thenReturn(false);
        TrackCommand trackCommand = new TrackCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock("/track https://example.com/", 1L);
        SendMessage actualMessage = trackCommand.authorizedHandle(updateMock, 1L);

        SendMessage expectedMessage = new SendMessage(1L, "Resource not supported.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_NotCallAuthorizedHandle_WhenUserNotFound() {
        TrackCommand trackCommand = spy(new TrackCommand(chatService, trackableLinkService));
        when(chatService.existsById(1L)).thenReturn(false);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        trackCommand.handle(updateMock);

        verify(trackCommand, never()).authorizedHandle(any(Update.class), any(Long.class));
    }
}
