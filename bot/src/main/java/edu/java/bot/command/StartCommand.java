package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.UserService;
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
        StringBuilder message = new StringBuilder();
        message.append("""
            Hello, *%s*!
            You are successfully registered. Pls, use commands:
            """.formatted(name));
        for (var command : commandHandlers) {
            message.append("- %s - %s.\n".formatted(command.command(), command.description()));
        }
        return new SendMessage(update.message().chat().id(), message.toString())
            .parseMode(ParseMode.Markdown);
    }
}
