package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class TrackCommand implements Command {

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking link";
    }

    @Override
    public AbstractSendRequest<?> handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Process /track...");
    }
}
