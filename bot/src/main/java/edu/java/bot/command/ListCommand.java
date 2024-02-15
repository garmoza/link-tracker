package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.service.UserService;
import java.util.Set;

public class ListCommand extends AuthorizedCommand {

    private final UserService userService;

    public ListCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of tracked links";
    }

    @Override
    public SendMessage authorizedHandle(Update update, User user) {
        long chatId = update.message().chat().id();

        Set<Link> links = user.getLinks();
        if (links.isEmpty()) {
            return new SendMessage(chatId, "There are no tracked links.");
        }

        StringBuilder message = new StringBuilder();
        message.append("Tracked links:\n");
        for (var link : links) {
            message.append("- %s\n".formatted(link.getUrl()));
        }
        return new SendMessage(chatId, message.toString())
            .parseMode(ParseMode.Markdown);
    }

    @Override
    UserService getUserService() {
        return userService;
    }
}
