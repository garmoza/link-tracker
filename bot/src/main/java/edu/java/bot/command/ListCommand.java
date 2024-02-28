package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.service.UserService;
import edu.java.bot.util.SendMessageFormatter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand extends AuthorizedCommand {

    private final UserService userService;

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
        SendMessageFormatter formatter = new SendMessageFormatter(update);

        Set<Link> links = user.getLinks();
        if (links.isEmpty()) {
            formatter.append("There are no tracked links.");
        } else {
            formatter.append("Tracked links:\n");
            formatter.appendLinkList(links);
        }

        return formatter.getMessage();
    }

    @Override
    UserService getUserService() {
        return userService;
    }
}
