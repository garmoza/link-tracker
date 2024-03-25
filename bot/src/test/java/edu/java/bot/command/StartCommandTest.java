package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.mock.MockCommandUtils;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.service.ChatService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private ChatService chatService;

    @Test
    void command() {
        CommandHandler startCommand = new StartCommand(chatService, List.of());

        String actualCommand = startCommand.command();

        assertEquals("/start", actualCommand);
    }

    @Test
    void description() {
        CommandHandler startCommand = new StartCommand(chatService, List.of());

        String actualDescription = startCommand.description();

        assertEquals("register a user", actualDescription);
    }

    @Test
    void handle_WithCommands() {
        CommandHandler commandHandler1 = MockCommandUtils.getCommandMock("/command", "description of command");
        CommandHandler commandHandler2 = MockCommandUtils.getCommandMock("/another", "another description");
        CommandHandler startCommand = new StartCommand(chatService, List.of(commandHandler1, commandHandler2));

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            */another* - another description
            */command* - description of command
            */start* - register a user
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_WithoutOtherCommands() {
        CommandHandler startCommand = new StartCommand(chatService, List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            */start* - register a user
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_AddsNewUser() {
        doNothing().when(chatService).registerChat(1L);
        CommandHandler startCommand = new StartCommand(chatService, List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, "Bob");
        startCommand.handle(updateMock);

        verify(chatService).registerChat(1L);
    }
}
