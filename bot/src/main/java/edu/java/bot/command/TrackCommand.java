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

public class TrackCommand extends AuthorizedCommand {

    private final UserService userService;
    private final TrackedLinkService trackedLinkService;

    public TrackCommand(UserService userService, TrackedLinkService trackedLinkService) {
        this.userService = userService;
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

        if (formatter.isEmpty() && !trackedLinkService.trackLink(user, link)) {
            formatter.append("Link already tracked.");
        }

        if (formatter.isEmpty()) {
            formatter.append("Link *%s* successfully added.", params[1]);
        }

        return formatter.getMessage();
    }

    @Override
    UserService getUserService() {
        return userService;
    }
}
