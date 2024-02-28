package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.UserService;
import edu.java.bot.util.SendMessageFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements CommandHandler {

    private final UserService userService;
    private final List<CommandHandler> commandHandlers;

    public StartCommand(UserService userService, @Lazy List<CommandHandler> commandHandlers) {
        this.userService = userService;
        this.commandHandlers = commandHandlers;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register a user";
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().from().id();
        userService.saveUser(userId);
        return getStartMessage(update);
    }

    private SendMessage getStartMessage(Update update) {
        String name = update.message().from().firstName();
        SendMessageFormatter formatter = new SendMessageFormatter(update);
        formatter.append("""
            Hello, *%s*!
            You are successfully registered. Pls, use commands:
            """, name);
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
