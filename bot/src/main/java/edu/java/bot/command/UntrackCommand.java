package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.Set;

public class UntrackCommand extends AuthorizedCommand {

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
    SendMessage authorizedHandle(Update update, User user) {
        long chatId = update.message().chat().id();
        String[] params = update.message().text().split(" ");

        if (params.length != 2) {
            return new SendMessage(chatId, "Pls, enter the command in *" + command() + " link* format.")
                .parseMode(ParseMode.Markdown);
        }

        Set<String> links = user.getLinks();

        if (!links.contains(params[1])) {
            return new SendMessage(chatId, "Tracking link not found.");
        }

        links.remove(params[1]);
        return new SendMessage(chatId, "Link *" + params[1] + "* no longer tracked.")
            .parseMode(ParseMode.Markdown);
    }

    @Override
    UserRepository getUserRepository() {
        return userRepository;
    }
}
