package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.service.ChatService;
import edu.java.bot.service.TrackableLinkService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCommandTest {

    @Mock
    private ChatService chatService;
    @Mock
    private TrackableLinkService trackableLinkService;

    @Test
    void command() {
        CommandHandler listCommand = new ListCommand(chatService, trackableLinkService);

        String actualCommand = listCommand.command();

        assertEquals("/list", actualCommand);
    }

    @Test
    void description() {
        CommandHandler listCommand = new ListCommand(chatService, trackableLinkService);

        String actualDescription = listCommand.description();

        assertEquals("show list of tracked links", actualDescription);
    }

    @Test
    void handle_UserIsNotRegistered() {
        when(chatService.existsById(1L)).thenReturn(false);
        CommandHandler listCommand = new ListCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = listCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "User is not registered.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_UserHasNoLinks() {
        when(chatService.existsById(1L)).thenReturn(true);
        when(trackableLinkService.getTrackableLinks(1L)).thenReturn(List.of());
        CommandHandler listCommand = new ListCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = listCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "There are no tracked links.")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_UserHasTrackingLinks() {
        when(chatService.existsById(1)).thenReturn(true);
        when(trackableLinkService.getTrackableLinks(1L)).thenReturn(List.of("url1", "url2"));
        CommandHandler listCommand = new ListCommand(chatService, trackableLinkService);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = listCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Tracked links:
            - url1
            - url2
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
