package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.util.SendMessageFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements CommandHandler {

    private final List<CommandHandler> commandHandlers;

    public HelpCommand(@Lazy List<CommandHandler> commandHandlers) {
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
        formatter.appendHandlerList(getAllCommandHandlers());
        return formatter.getMessage();
    }

    private List<CommandHandler> getAllCommandHandlers() {
        List<CommandHandler> allCommands = new ArrayList<>(commandHandlers);
        allCommands.add(this);
        allCommands.sort(Comparator.comparing(CommandHandler::command));
        return allCommands;
    }
}
