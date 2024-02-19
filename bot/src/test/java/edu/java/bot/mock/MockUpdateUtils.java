package edu.java.bot.mock;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockUpdateUtils {

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
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User user = mock(User.class);

        // stubbing
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().from()).thenReturn(user);
        when(update.message().from().id()).thenReturn(userId);

        return update;
    }

    public static Update getUpdateMock(long chatId, long userId, String firstName) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User user = mock(User.class);

        // stubbing
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().from()).thenReturn(user);
        when(update.message().from().id()).thenReturn(userId);
        when(update.message().from().firstName()).thenReturn(firstName);

        return update;
    }

    public static Update getUpdateMock(String messageText, long chatId) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        // stubbing
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(messageText);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(chatId);

        return update;
    }

    public static Update getUpdateMock(String messageText) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        // stubbing
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(messageText);

        return update;
    }
}
