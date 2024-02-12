package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.Optional;
import java.util.Set;

public class UntrackCommand implements Command {

    private final UserRepository userRepository;

    public UntrackCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking link";
    }

    @Override
    public AbstractSendRequest<?> handle(Update update) {
        long chatId = update.message().chat().id();
        long userId = update.message().from().id();
        String[] params = update.message().text().split(" ");

        Optional<User> userOptional = userRepository.findUserById(userId);
        if (userOptional.isEmpty()) {
            return new SendMessage(chatId, "User is not registered.");
        }

        if (params.length == 1) {
            return new SendMessage(chatId, "Pls, enter the command in *" + command() + " link* format.")
                .parseMode(ParseMode.Markdown);
        }

        if (params.length != 2) {
            return new SendMessage(chatId, "Not valid format of " + command() + " command.");
        }

        User user = userOptional.get();
        Set<String> links = user.getLinks();

        if (!links.contains(params[1])) {
            return new SendMessage(chatId, "Tracking link not found.");
        }

        links.remove(params[1]);
        return new SendMessage(chatId, "Link *" + params[1] + "* no longer tracked.")
            .parseMode(ParseMode.Markdown);
    }
}
