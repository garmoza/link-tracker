package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface Command {

    String command();

    String description();

    default boolean supports(Update update) {
        return update.message().text().startsWith(command());
    }

    AbstractSendRequest<?> handle(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
