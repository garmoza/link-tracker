package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.processor.UpdateProcessor;
import java.util.List;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private List<CommandHandler> commandHandlers;
    @Mock
    private UpdateProcessor updateProcessor;

    @Test
    void setUpBot_CallSetUpdatesListener() {
        doNothing().when(telegramBot).setUpdatesListener(any(UpdatesListener.class));

        Bot bot = new Bot(telegramBot, commandHandlers, updateProcessor);

        verify(telegramBot).setUpdatesListener(bot);
    }

    @Test
    void setUpBot_setUpMenuCommands() {
        CommandHandler commandHandler = mock(CommandHandler.class);
        when(commandHandler.toApiCommand()).thenReturn(new BotCommand("/command", "description"));
        List<CommandHandler> commandHandlers = List.of(commandHandler);

        Bot bot = new Bot(telegramBot, commandHandlers, updateProcessor);

        ArgumentCaptor<SetMyCommands> captor = ArgumentCaptor.forClass(SetMyCommands.class);
        verify(telegramBot).execute(captor.capture());
        SetMyCommands actual = captor.getValue();
        SetMyCommands expected = new SetMyCommands(Arrays.array(new BotCommand("/command", "description")));
        var actualBotCommands = (BotCommand[]) actual.getParameters().get("commands");
        var expectedBotCommands = (BotCommand[]) expected.getParameters().get("commands");
        assertNotNull(actualBotCommands[0]);
        assertEquals(expectedBotCommands[0], actualBotCommands[0]);
    }

    @Test
    void process_ResponseWithSendMessage() {
        when(updateProcessor.process(any(Update.class))).thenReturn(new SendMessage(1L, "message"));

        Bot bot = new Bot(telegramBot, commandHandlers, updateProcessor);
        Update updateMock = mock(Update.class);
        Message message = mock(Message.class);
        when(updateMock.message()).thenReturn(message);
        bot.process(List.of(updateMock));

        verify(telegramBot).execute(any(SendMessage.class));
    }
}
