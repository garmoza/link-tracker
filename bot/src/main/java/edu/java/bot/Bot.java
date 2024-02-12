package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class Bot implements UpdatesListener {

    private final TelegramBot bot;
    private final UserRepository userRepository;
    private final List<Command> commands;

    public Bot() {
        bot = new TelegramBot(System.getenv("TELEGRAM_API_KEY"));
        userRepository = new UserRepository();

        commands = new ArrayList<>();
        commands.add(new HelpCommand(commands));
        commands.add(new ListCommand(userRepository));
        commands.add(new StartCommand(userRepository, commands));
        commands.add(new TrackCommand(userRepository));
        commands.add(new UntrackCommand(userRepository));
    }

    public void start() {
        bot.setUpdatesListener(this);
        setUpMenuCommands();
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            if (update.message().text().startsWith("/")) {
                processCommand(update);
            } else {
                processMessage(update);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processCommand(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                bot.execute(command.handle(update));
            }
        }
    }

    private void processMessage(Update update) {
        bot.execute(new SendMessage(update.message().chat().id(), "Process message..."));
    }

    private void setUpMenuCommands() {
        bot.execute(new SetMyCommands(commands.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new)));
    }
}
