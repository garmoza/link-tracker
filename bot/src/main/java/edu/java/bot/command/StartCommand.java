package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.UserService;
import edu.java.bot.util.SendMessageFormatter;
import java.util.List;

public class StartCommand implements CommandHandler {

    private final UserService userService;
    private final List<CommandHandler> commandHandlers;

    public StartCommand(UserService userService, List<CommandHandler> commandHandlers) {
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
        formatter.appendHandlerList(commandHandlers);
        return formatter.getMessage();
    }
}
