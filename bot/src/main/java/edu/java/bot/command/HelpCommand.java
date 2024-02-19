package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.util.SendMessageFormatter;
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
        SendMessageFormatter formatter = new SendMessageFormatter(update);
        formatter.appendHandlerList(commandHandlers);
        return formatter.getMessage();
    }
}
