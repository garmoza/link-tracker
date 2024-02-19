package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandHandler {

    String command();

    String description();

    default boolean supports(Update update) {
        String[] params = update.message().text().split(" ");
        return params.length != 0 && params[0].equals(command());
    }

    SendMessage handle(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
