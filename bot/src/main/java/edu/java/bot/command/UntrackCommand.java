package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class UntrackCommand implements Command {

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking link";
    }

    @Override
    public AbstractSendRequest<?> handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Process /untrack...");
    }
}
