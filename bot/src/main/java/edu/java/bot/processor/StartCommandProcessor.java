package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;

public class StartCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;
    private final UserRepository userRepository;

    public StartCommandProcessor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractSendRequest<?> process(Update update) {
        if (update.message().text().equals("/start")) {
            long userId = update.message().from().id();
            boolean isNewUser = userRepository.findUserById(userId).isEmpty();
            if (isNewUser) {
                userRepository.saveUser(new User(userId));
            }
            return getStartMessage(update);
        }
        if (nextProcessor != null) {
            return nextProcessor.process(update);
        }
        return new SendMessage(update.message().chat().id(), "Command not found");
    }

    private AbstractSendRequest<?> getStartMessage(Update update) {
        String name = update.message().from().firstName();
        String message = """
            Hello, *%s*!
            You are successfully registered. Pls use commands:
            - */help* - display all available commands.
            - */track* - start tracking link.
            - */untrack* - stop tracking link.
            - */list* - show list of tracked links.
            """.formatted(name);
        return new SendMessage(update.message().chat().id(), message)
            .parseMode(ParseMode.Markdown);
    }

    @Override
    public void nextCommandProcessor(CommandProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }
}
