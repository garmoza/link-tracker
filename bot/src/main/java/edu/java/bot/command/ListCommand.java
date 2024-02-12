package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.Optional;
import java.util.Set;

public class ListCommand implements Command {

    private final UserRepository userRepository;

    public ListCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of tracked links";
    }

    @Override
    public AbstractSendRequest<?> handle(Update update) {
        long chatId = update.message().chat().id();
        long userId = update.message().from().id();

        Optional<User> userOptional = userRepository.findUserById(userId);
        if (userOptional.isEmpty()) {
            return new SendMessage(chatId, "User is not registered.");
        }

        User user = userOptional.get();
        Set<String> links = user.getLinks();
        if (links.isEmpty()) {
            return new SendMessage(chatId, "There are no tracked links.");
        }

        StringBuilder message = new StringBuilder();
        message.append("Tracked links:\n");
        for (var link : links) {
            message.append("- %s\n".formatted(link));
        }
        return new SendMessage(chatId, message.toString());
    }
}
