package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

public class HelpCommand implements CommandHandler {

    private final List<CommandHandler> commandHandlers;

    public HelpCommand(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
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
    public SendMessage handle(Update update) {
        StringBuilder message = new StringBuilder();
        for (var command : commandHandlers) {
            message.append("*%s* - %s\n".formatted(command.command(), command.description()));
        }
        return new SendMessage(update.message().chat().id(), message.toString())
            .parseMode(ParseMode.Markdown);
    }
}
