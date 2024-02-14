package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.TrackedLinkService;
import edu.java.bot.util.LinkParser;
import edu.java.bot.util.URLParseException;

public class TrackCommand extends AuthorizedCommand {

    private final UserRepository userRepository;
    private final TrackedLinkService trackedLinkService;

    public TrackCommand(UserRepository userRepository, TrackedLinkService trackedLinkService) {
        this.userRepository = userRepository;
        this.trackedLinkService = trackedLinkService;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking link";
    }

    @Override
    SendMessage authorizedHandle(Update update, User user) {
        long chatId = update.message().chat().id();
        String[] params = update.message().text().split(" ");

        if (params.length != 2) {
            return new SendMessage(chatId, "Pls, enter the command in *" + command() + " link* format.")
                .parseMode(ParseMode.Markdown);
        }

        Link link;
        try {
            link = LinkParser.parse(params[1]);
        } catch (URLParseException e) {
            return new SendMessage(chatId, e.getMessage());
        }

        boolean linkAdded = trackedLinkService.trackLink(user, link);

        if (!linkAdded) {
            return new SendMessage(chatId, "Link already tracking.");
        }

        return new SendMessage(chatId, "Link *" + params[1] + "* successfully added.")
            .parseMode(ParseMode.Markdown);
    }

    @Override
    UserRepository getUserRepository() {
        return userRepository;
    }
}
