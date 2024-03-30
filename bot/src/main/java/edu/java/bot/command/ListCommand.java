package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ChatService;
import edu.java.bot.service.TrackableLinkService;
import edu.java.bot.util.SendMessageFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand extends AuthorizedCommand {

    private final ChatService chatService;
    private final TrackableLinkService trackableLinkService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of tracked links";
    }

    @Override
    public SendMessage authorizedHandle(Update update, long chatId) {
        SendMessageFormatter formatter = new SendMessageFormatter(update);

        List<String> links = trackableLinkService.getTrackableLinks(chatId);
        if (links.isEmpty()) {
            formatter.append("There are no tracked links.");
        } else {
            formatter.append("Tracked links:\n");
            formatter.appendLinkList(links);
        }

        return formatter.getMessage();
    }

    @Override
    ChatService getChatService() {
        return chatService;
    }
}
