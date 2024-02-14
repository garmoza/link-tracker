package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import java.util.List;

public class UpdateService {

    private final List<Command> commands;

    public UpdateService(List<Command> commands) {
        this.commands = commands;
    }

    public SendMessage process(Update update) {
        if (update.message().text().startsWith("/")) {
            return processCommand(update);
        } else {
            return processText(update);
        }
    }

    private SendMessage processCommand(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(update.message().chat().id(), "Command not supported.");
    }

    private SendMessage processText(Update update) {
        return new SendMessage(update.message().chat().id(), "Process text...");
    }
}