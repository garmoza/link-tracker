package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
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
import edu.java.bot.service.UserService;
import edu.java.bot.service.impl.TrackedLinkServiceImpl;
import edu.java.bot.service.impl.UserServiceImpl;
import edu.java.bot.source.GitHubTrackableLink;
import edu.java.bot.source.StackOverflowTrackableLink;
import edu.java.bot.source.TrackableLink;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);

        Bot bot = createBot();
        bot.start();
    }

    private static Bot createBot() {
        ApplicationConfig appConfig = new ApplicationConfig(System.getenv("TELEGRAM_API_KEY"));
        TelegramBot telegramBot = new TelegramBot(appConfig.telegramToken());

        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);

        List<TrackableLink> trackableLinks = new ArrayList<>();
        trackableLinks.add(new GitHubTrackableLink());
        trackableLinks.add(new StackOverflowTrackableLink());
        TrackedLinkService trackedLinkService = new TrackedLinkServiceImpl(trackableLinks);

        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new HelpCommand(commandHandlers));
        commandHandlers.add(new ListCommand(userService));
        commandHandlers.add(new StartCommand(userService, commandHandlers));
        commandHandlers.add(new TrackCommand(userService, trackedLinkService));
        commandHandlers.add(new UntrackCommand(userService, trackedLinkService));

        UpdateProcessor updateProcessor = new UpdateProcessor(commandHandlers);

        return new Bot(telegramBot, commandHandlers, updateProcessor);
    }
}
