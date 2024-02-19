package edu.java.bot.mock;

import edu.java.bot.command.CommandHandler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockCommandUtils {

    public static CommandHandler getCommandMock(String commandName, String description) {
        CommandHandler commandHandler = mock(CommandHandler.class);

        // stubbing
        when(commandHandler.command()).thenReturn(commandName);
        when(commandHandler.description()).thenReturn(description);

        return commandHandler;
    }
}
