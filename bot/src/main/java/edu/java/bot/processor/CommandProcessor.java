package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface CommandProcessor {

    AbstractSendRequest<?> process(Update update);

    void nextCommandProcessor(CommandProcessor nextProcessor);
}
