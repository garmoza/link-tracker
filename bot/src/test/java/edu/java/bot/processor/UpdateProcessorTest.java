package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.mock.MockUpdateUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProcessorTest {

    @Test
    void process_ProcessText() {
        UpdateProcessor updateProcessor = new UpdateProcessor(List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock("Test message without command", 1L);
        SendMessage actualMessage = updateProcessor.process(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "Process text...");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void process_ProcessCommand_CommandSupported() {
        CommandHandler commandHandler = Mockito.mock(CommandHandler.class);
        when(commandHandler.supports(any(Update.class))).thenReturn(true);
        when(commandHandler.handle(any(Update.class))).thenReturn(new SendMessage(1L, "Text"));
        UpdateProcessor updateProcessor = new UpdateProcessor(List.of(commandHandler));

        Update updateMock = MockUpdateUtils.getUpdateMock("/commandHandler param1 param2", 1L);
        updateProcessor.process(updateMock);

        verify(commandHandler).handle(any(Update.class));
    }

    @Test
    void process_ProcessCommand_CommandNotSupported() {
        CommandHandler commandHandler = Mockito.mock(CommandHandler.class);
        when(commandHandler.supports(any(Update.class))).thenReturn(false);
        UpdateProcessor updateProcessor = new UpdateProcessor(List.of(commandHandler));

        Update updateMock = MockUpdateUtils.getUpdateMock("/command param1 param2", 1L);
        SendMessage actualMessage = updateProcessor.process(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "Command not supported.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
