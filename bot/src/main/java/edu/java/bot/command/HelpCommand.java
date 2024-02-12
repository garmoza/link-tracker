package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

public class HelpCommand implements Command {

    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "display all available commands";
    }

    @Override
    public AbstractSendRequest<?> handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Process /help...");
    }
}
