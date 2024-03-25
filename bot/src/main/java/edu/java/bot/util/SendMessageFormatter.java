package edu.java.bot.util;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandHandler;
import java.util.Collection;

public class SendMessageFormatter {

    private final long chatId;
    private final StringBuilder builder;

    public SendMessageFormatter(Update update) {
        chatId = update.message().chat().id();
        builder = new StringBuilder();
    }

    public boolean isEmpty() {
        return builder.isEmpty();
    }

    public void append(String message, Object... args) {
        builder.append(message.formatted(args));
    }

    public void appendHandlerList(Collection<CommandHandler> commandHandlers) {
        for (var command : commandHandlers) {
            builder.append("*%s* - %s\n".formatted(command.command(), command.description()));
        }
    }

    public void appendLinkList(Collection<String> links) {
        for (var link : links) {
            builder.append("- %s\n".formatted(link));
        }
    }

    public SendMessage getMessage() {
        return new SendMessage(chatId, toString())
            .parseMode(ParseMode.Markdown);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
