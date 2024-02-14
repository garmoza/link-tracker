package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.mock.MockUpdateUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateServiceTest {

    @Test
    void process_ProcessText() {
        UpdateService updateService = new UpdateService(List.of());

        Update updateMock = MockUpdateUtils.getUpdateMock("Test message without command", 1L);
        SendMessage actualMessage = updateService.process(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "Process text...");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void process_ProcessCommand_CommandSupported() {
        Command command = Mockito.mock(Command.class);
        when(command.supports(any(Update.class))).thenReturn(true);
        when(command.handle(any(Update.class))).thenReturn(new SendMessage(1L, "Text"));
        UpdateService updateService = new UpdateService(List.of(command));

        Update updateMock = MockUpdateUtils.getUpdateMock("/command param1 param2", 1L);
        updateService.process(updateMock);

        verify(command).handle(any(Update.class));
    }

    @Test
    void process_ProcessCommand_CommandNotSupported() {
        Command command = Mockito.mock(Command.class);
        when(command.supports(any(Update.class))).thenReturn(false);
        UpdateService updateService = new UpdateService(List.of(command));

        Update updateMock = MockUpdateUtils.getUpdateMock("/command param1 param2", 1L);
        SendMessage actualMessage = updateService.process(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "Command not supported.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}