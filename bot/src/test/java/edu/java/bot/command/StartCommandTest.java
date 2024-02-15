package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.command.StartCommand;
import edu.java.bot.entity.User;
import edu.java.bot.mock.MockCommandUtils;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private UserService userService;

    @Test
    void command() {
        CommandHandler startCommand = new StartCommand(userService, List.of());

        String actualCommand = startCommand.command();

        assertEquals("/start", actualCommand);
    }

    @Test
    void description() {
        CommandHandler startCommand = new StartCommand(userService, List.of());

        String actualDescription = startCommand.description();

        assertEquals("register a user", actualDescription);
    }

    @Test
    void handle_WithCommands() {
        CommandHandler commandHandler1 = MockCommandUtils.getCommandMock("/command", "description of command");
        CommandHandler commandHandler2 = MockCommandUtils.getCommandMock("/another", "another description");
        CommandHandler startCommand = new StartCommand(userService, List.of(commandHandler1, commandHandler2));

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            - /command - description of command.
            - /another - another description.
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_WithoutCommands() {
        CommandHandler startCommand = new StartCommand(userService, List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_WithSelfRef() {
        List<CommandHandler> commandHandlers = new ArrayList<>();
        CommandHandler startCommand = new StartCommand(userService, commandHandlers);
        commandHandlers.add(startCommand);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            - /start - register a user.
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_AddsNewUser() {
        when(userService.saveUser(2L)).thenReturn(new User(2L));
        CommandHandler startCommand = new StartCommand(userService, List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L, "Bob");
        startCommand.handle(updateMock);

        verify(userService).saveUser(2L);
    }
}
