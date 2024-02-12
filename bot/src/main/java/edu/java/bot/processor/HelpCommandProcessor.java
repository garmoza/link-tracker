package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommandProcessor implements CommandProcessor {

    private CommandProcessor nextProcessor;

    @Override
    public AbstractSendRequest<?> process(Update update) {
        if (update.message().text().equals("/help")) {
            return new SendMessage(update.message().chat().id(), "Processing /help");
        }
        if (nextProcessor != null) {
            return nextProcessor.process(update);
        }
        return new SendMessage(update.message().chat().id(), "Command not found");
    }

    @Override
    public void nextCommandProcessor(CommandProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }
}
