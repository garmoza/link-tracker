package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ChatService;

public abstract class AuthorizedCommand implements CommandHandler {

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        ChatService chatService = getChatService();

        if (!chatService.existsById(chatId)) {
            return new SendMessage(chatId, "User is not registered.");
        }

        return authorizedHandle(update, chatId);
    }

    abstract SendMessage authorizedHandle(Update update, long chatId);

    abstract ChatService getChatService();
}
