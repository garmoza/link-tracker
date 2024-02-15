package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.processor.UpdateProcessor;
import java.util.List;

public class Bot implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final List<CommandHandler> commandHandlers;
    private final UpdateProcessor updateProcessor;

    public Bot(TelegramBot telegramBot, List<CommandHandler> commandHandlers, UpdateProcessor updateProcessor) {
        this.telegramBot = telegramBot;
        this.commandHandlers = commandHandlers;
        this.updateProcessor = updateProcessor;
    }

    public void start() {
        telegramBot.setUpdatesListener(this);
        setUpMenuCommands();
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            telegramBot.execute(updateProcessor.process(update));
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void setUpMenuCommands() {
        telegramBot.execute(new SetMyCommands(commandHandlers.stream()
            .map(CommandHandler::toApiCommand)
            .toArray(BotCommand[]::new)));
    }
}
