package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.mock.MockUpdateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandTest {

    @Spy
    private Command command;

    @Test
    void supports_ReturnsTrue() {
        when(command.command()).thenReturn("/command");
        Update updateMock = MockUpdateUtils.getUpdateMock("/command param1 param2");

        boolean isSupports = command.supports(updateMock);

        assertThat(isSupports).as("checks that message start with command").isTrue();
    }

    @Test
    void supports_ReturnsFalse() {
        when(command.command()).thenReturn("/command");
        Update updateMock = MockUpdateUtils.getUpdateMock("/another param1 param2");

        boolean isSupports = command.supports(updateMock);

        assertThat(isSupports).as("checks that message does not start with command").isFalse();
    }

    @Test
    void toApiCommand() {
        when(command.command()).thenReturn("/command");
        when(command.description()).thenReturn("fancy description");

        BotCommand actualBotCommand = command.toApiCommand();

        BotCommand expectedBotCommand = new BotCommand("/command", "fancy description");
        assertEquals(expectedBotCommand, actualBotCommand);
    }
}
