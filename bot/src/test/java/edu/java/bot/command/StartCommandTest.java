package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.mock.CommandMockUtils;
import edu.java.bot.mock.UpdateMockUtils;
import edu.java.bot.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void command() {
        Command startCommand = new StartCommand(userRepository, List.of());

        String actualCommand = startCommand.command();

        assertEquals("/start", actualCommand);
    }

    @Test
    void description() {
        Command startCommand = new StartCommand(userRepository, List.of());

        String actualDescription = startCommand.description();

        assertEquals("register a user", actualDescription);
    }

    @Test
    void handleWithCommands() {
        Command command1 = CommandMockUtils.getCommandMock("/command", "description of command");
        Command command2 = CommandMockUtils.getCommandMock("/another", "another description");
        when(userRepository.findUserById(2L)).thenReturn(Optional.of(new User(2L)));
        Command startCommand = new StartCommand(userRepository, List.of(command1, command2));

        Update updateMock = UpdateMockUtils.getUpdateMock(1L, 2L, "Bob");
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
    void handleWithoutCommands() {
        when(userRepository.findUserById(2L)).thenReturn(Optional.of(new User(2L)));
        Command startCommand = new StartCommand(userRepository, List.of());

        Update updateMock = UpdateMockUtils.getUpdateMock(1L, 2L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handleWithSelfRef() {
        List<Command> commands = new ArrayList<>();
        when(userRepository.findUserById(2L)).thenReturn(Optional.of(new User(2L)));
        Command startCommand = new StartCommand(userRepository, commands);
        commands.add(startCommand);

        Update updateMock = UpdateMockUtils.getUpdateMock(1L, 2L, "Bob");
        SendMessage actualMessage = startCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Hello, *Bob*!
            You are successfully registered. Pls, use commands:
            - /start - register a user.
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
