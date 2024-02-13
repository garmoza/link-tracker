package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.mock.MockCommandUtils;
import edu.java.bot.mock.MockUpdateUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpCommandTest {

    @Test
    void command() {
        Command helpCommand = new HelpCommand(new ArrayList<>());

        String actualCommand = helpCommand.command();

        assertEquals("/help", actualCommand);
    }

    @Test
    void description() {
        Command helpCommand = new HelpCommand(new ArrayList<>());

        String actualDescription = helpCommand.description();

        assertEquals("display all available commands", actualDescription);
    }

    @Test
    void handle_WithCommands() {
        Command command1 = MockCommandUtils.getCommandMock("/command", "description of command");
        Command command2 = MockCommandUtils.getCommandMock("/another", "another description");
        Command helpCommand = new HelpCommand(List.of(command1, command2));

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = helpCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            */command* - description of command
            */another* - another description
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_WithoutCommands() {
        Command helpCommand = new HelpCommand(List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = helpCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_WithSelfRef() {
        List<Command> commands = new ArrayList<>();
        Command helpCommand = new HelpCommand(commands);
        commands.add(helpCommand);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L);
        SendMessage actualMessage = helpCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            */help* - display all available commands
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
