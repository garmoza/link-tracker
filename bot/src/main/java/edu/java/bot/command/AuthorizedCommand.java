package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.Optional;

public abstract class AuthorizedCommand implements Command {

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        long userId = update.message().from().id();

        Optional<User> userOptional = getUserRepository().findUserById(userId);
        if (userOptional.isEmpty()) {
            return new SendMessage(chatId, "User is not registered.");
        }

        return authorizedHandle(update, userOptional.get());
    }

    abstract SendMessage authorizedHandle(Update update, User user);

    abstract UserRepository getUserRepository();
}
