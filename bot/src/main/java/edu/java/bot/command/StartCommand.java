package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.List;

public class StartCommand implements CommandHandler {

    private final UserRepository userRepository;
    private final List<CommandHandler> commandHandlers;

    public StartCommand(UserRepository userRepository, List<CommandHandler> commandHandlers) {
        this.userRepository = userRepository;
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
        boolean isNewUser = userRepository.findUserById(userId).isEmpty();
        if (isNewUser) {
            userRepository.saveUser(new User(userId));
        }
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
