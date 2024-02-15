package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.source.GitHubTrackableLink;
import edu.java.bot.source.StackOverflowTrackableLink;
import edu.java.bot.source.TrackableLink;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserRepositoryImpl;
import edu.java.bot.service.TrackedLinkService;
import edu.java.bot.service.TrackedLinkServiceImpl;
import edu.java.bot.processor.UpdateProcessor;
import java.util.ArrayList;
import java.util.List;

public class Bot implements UpdatesListener {

    private final TelegramBot bot;
    private final List<Command> commands;
    private final UpdateProcessor updateProcessor;

    public Bot() {
        ApplicationConfig appConfig = new ApplicationConfig(System.getenv("TELEGRAM_API_KEY"));
        bot = new TelegramBot(appConfig.telegramToken());
        UserRepository userRepository = new UserRepositoryImpl();

        List<TrackableLink> trackableLinks = new ArrayList<>();
        trackableLinks.add(new GitHubTrackableLink());
        trackableLinks.add(new StackOverflowTrackableLink());
        TrackedLinkService trackedLinkService = new TrackedLinkServiceImpl(trackableLinks);

        commands = new ArrayList<>();
        commands.add(new HelpCommand(commands));
        commands.add(new ListCommand(userRepository));
        commands.add(new StartCommand(userRepository, commands));
        commands.add(new TrackCommand(userRepository, trackedLinkService));
        commands.add(new UntrackCommand(userRepository, trackedLinkService));

        updateProcessor = new UpdateProcessor(commands);
    }

    public void start() {
        bot.setUpdatesListener(this);
        setUpMenuCommands();
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            bot.execute(updateProcessor.process(update));
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void setUpMenuCommands() {
        bot.execute(new SetMyCommands(commands.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new)));
    }
}
