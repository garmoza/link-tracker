package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processor.UpdateProcessor;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserRepositoryImpl;
import edu.java.bot.service.TrackedLinkService;
import edu.java.bot.service.TrackedLinkServiceImpl;
import edu.java.bot.service.UserService;
import edu.java.bot.service.UserServiceImpl;
import edu.java.bot.source.GitHubTrackableLink;
import edu.java.bot.source.StackOverflowTrackableLink;
import edu.java.bot.source.TrackableLink;
import java.util.ArrayList;
import java.util.List;

public class Bot implements UpdatesListener {

    private final TelegramBot bot;
    private final List<CommandHandler> commandHandlers;
    private final UpdateProcessor updateProcessor;

    public Bot() {
        ApplicationConfig appConfig = new ApplicationConfig(System.getenv("TELEGRAM_API_KEY"));
        bot = new TelegramBot(appConfig.telegramToken());
        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);

        List<TrackableLink> trackableLinks = new ArrayList<>();
        trackableLinks.add(new GitHubTrackableLink());
        trackableLinks.add(new StackOverflowTrackableLink());
        TrackedLinkService trackedLinkService = new TrackedLinkServiceImpl(trackableLinks);

        commandHandlers = new ArrayList<>();
        commandHandlers.add(new HelpCommand(commandHandlers));
        commandHandlers.add(new ListCommand(userRepository));
        commandHandlers.add(new StartCommand(userService, commandHandlers));
        commandHandlers.add(new TrackCommand(userRepository, trackedLinkService));
        commandHandlers.add(new UntrackCommand(userRepository, trackedLinkService));

        updateProcessor = new UpdateProcessor(commandHandlers);
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
        bot.execute(new SetMyCommands(commandHandlers.stream()
            .map(CommandHandler::toApiCommand)
            .toArray(BotCommand[]::new)));
    }
}
