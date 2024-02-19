package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.service.TrackedLinkService;
import edu.java.bot.service.UserService;
import edu.java.bot.util.LinkParser;
import edu.java.bot.util.SendMessageFormatter;
import edu.java.bot.util.URLParseException;

public class UntrackCommand extends AuthorizedCommand {

    private final UserService userService;
    private final TrackedLinkService trackedLinkService;

    public UntrackCommand(UserService userService, TrackedLinkService trackedLinkService) {
        this.userService = userService;
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
        String[] params = update.message().text().split(" ");

        SendMessageFormatter formatter = new SendMessageFormatter(update);

        if (params.length != 2) {
            formatter.append("Pls, enter the command in *%s URL* format.", command());
        }

        Link link = null;
        if (formatter.isEmpty()) {
            try {
                link = LinkParser.parse(params[1]);
            } catch (URLParseException e) {
                formatter.append(e.getMessage());
            }
        }

        if (formatter.isEmpty() && !trackedLinkService.isTrackableLink(link)) {
            formatter.append("Resource not supported.");
        }

        if (formatter.isEmpty() && !trackedLinkService.untrackLink(user, link)) {
            formatter.append("Tracking link not found.");
        }

        if (formatter.isEmpty()) {
            formatter.append("Link *%s* no longer tracked.", params[1]);
        }

        return formatter.getMessage();
    }

    @Override
    UserService getUserService() {
        return userService;
    }
}
