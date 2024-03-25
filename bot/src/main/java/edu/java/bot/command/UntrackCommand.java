package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ChatService;
import edu.java.bot.service.TrackableLinkService;
import edu.java.bot.util.LinkParser;
import edu.java.bot.util.SendMessageFormatter;
import edu.java.bot.util.URLParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntrackCommand extends AuthorizedCommand {

    private final ChatService chatService;
    private final TrackableLinkService trackableLinkService;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking link";
    }

    @Override
    SendMessage authorizedHandle(Update update, long chatId) {
        String[] params = update.message().text().split(" ");

        SendMessageFormatter formatter = new SendMessageFormatter(update);

        if (params.length != 2) {
            formatter.append("Pls, enter the command in *%s URL* format.", command());
        }

        String link = null;
        if (formatter.isEmpty()) {
            try {
                link = LinkParser.parse(params[1]);
            } catch (URLParseException e) {
                formatter.append(e.getMessage());
            }
        }

        if (formatter.isEmpty() && !trackableLinkService.isTrackableLink(link)) {
            formatter.append("Resource not supported.");
        }

        if (formatter.isEmpty() && !trackableLinkService.unsubscribe(chatId, link)) {
            formatter.append("Tracking link not found.");
        }

        if (formatter.isEmpty()) {
            formatter.append("Link *%s* no longer tracked.", params[1]);
        }

        return formatter.getMessage();
    }

    @Override
    ChatService getChatService() {
        return chatService;
    }
}
