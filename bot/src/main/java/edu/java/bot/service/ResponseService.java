package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.HelpCommandProcessor;
import edu.java.bot.processor.ListCommandProcessor;
import edu.java.bot.processor.StartCommandProcessor;
import edu.java.bot.processor.TrackCommandProcessor;
import edu.java.bot.processor.UntrackCommandProcessor;
import edu.java.bot.repository.UserRepository;

public class ResponseService {

    private final UserRepository userRepository;

    public ResponseService() {
        this.userRepository = new UserRepository();
    }

    public AbstractSendRequest<?> getResponseRequest(Update update) {
        if (update.message().text().startsWith("/")) {
            return processCommand(update);
        }
        return processMessage(update);
    }

    private AbstractSendRequest<?> processCommand(Update update) {
        var startProcessor = new StartCommandProcessor(userRepository);
        var helpProcessor = new HelpCommandProcessor();
        var trackProcessor = new TrackCommandProcessor();
        var untrackProcessor = new UntrackCommandProcessor();
        var listProcessor = new ListCommandProcessor();

        startProcessor.nextCommandProcessor(helpProcessor);
        helpProcessor.nextCommandProcessor(trackProcessor);
        trackProcessor.nextCommandProcessor(untrackProcessor);
        untrackProcessor.nextCommandProcessor(listProcessor);

        return startProcessor.process(update);
    }

    private AbstractSendRequest<?> processMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Process message...");
    }
}
