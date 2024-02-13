package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandMocks {

    public static Command getCommandMock(String commandName, String description) {
        Command command = mock(Command.class);

        // stubbing
        when(command.command()).thenReturn(commandName);
        when(command.description()).thenReturn(description);

        return command;
    }

    public static Update getUpdateMock(long chatId) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        // stubbing
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(chatId);

        return update;
    }

    public static Update getUpdateMock(long chatId, long userId) {
        Update update = getUpdateMock(chatId);
        User user = mock(User.class);

        // stubbing
        when(update.message().from()).thenReturn(user);
        when(update.message().from().id()).thenReturn(userId);

        return update;
    }

    public static Update getUpdateMock(long chatId, long userId, String firstName) {
        Update update = getUpdateMock(chatId, userId);

        // stubbing
        when(update.message().from().firstName()).thenReturn(firstName);

        return update;
    }
}
