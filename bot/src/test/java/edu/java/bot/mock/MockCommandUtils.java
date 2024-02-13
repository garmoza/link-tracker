package edu.java.bot.mock;

import edu.java.bot.command.Command;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockCommandUtils {

    public static Command getCommandMock(String commandName, String description) {
        Command command = mock(Command.class);

        // stubbing
        when(command.command()).thenReturn(commandName);
        when(command.description()).thenReturn(description);

        return command;
    }
}
