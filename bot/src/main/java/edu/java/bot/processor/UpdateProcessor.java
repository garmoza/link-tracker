package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProcessor {

    private final List<CommandHandler> commandHandlers;

    public SendMessage process(Update update) {
        if (update.message().text().startsWith("/")) {
            return processCommand(update);
        } else {
            return processText(update);
        }
    }

    private SendMessage processCommand(Update update) {
        for (var command : commandHandlers) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(update.message().chat().id(), "Command not supported.");
    }

    private SendMessage processText(Update update) {
        return new SendMessage(update.message().chat().id(), "Process text...");
    }
}
