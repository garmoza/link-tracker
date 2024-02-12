package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class ListCommand implements Command {

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of tracked links";
    }

    @Override
    public AbstractSendRequest<?> handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Process /list...");
    }
}
