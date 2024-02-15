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
import java.util.Optional;

public class UntrackCommand extends AuthorizedCommand {

    private final UserRepository userRepository;
    private final TrackedLinkService trackedLinkService;

    public UntrackCommand(UserRepository userRepository, TrackedLinkService trackedLinkService) {
        this.userRepository = userRepository;
        this.trackedLinkService = trackedLinkService;
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

        Optional<String> resultMessage = Optional.empty();

        if (params.length != 2) {
            resultMessage = Optional.of("Pls, enter the command in *" + command() + " URL* format.");
        }

        Link link = null;
        if (resultMessage.isEmpty()) {
            try {
                link = LinkParser.parse(params[1]);
            } catch (URLParseException e) {
                resultMessage = Optional.of(e.getMessage());
            }
        }

        if (resultMessage.isEmpty() && !trackedLinkService.isTrackableLink(link)) {
            resultMessage = Optional.of("Resource not supported.");
        }

        if (resultMessage.isEmpty() && !trackedLinkService.untrackLink(user, link)) {
            resultMessage = Optional.of("Tracking link not found.");
        }

        if (resultMessage.isEmpty()) {
            resultMessage = Optional.of("Link *" + params[1] + "* no longer tracked.");
        }

        return new SendMessage(chatId, resultMessage.get())
            .parseMode(ParseMode.Markdown);
    }

    @Override
    UserRepository getUserRepository() {
        return userRepository;
    }
}
