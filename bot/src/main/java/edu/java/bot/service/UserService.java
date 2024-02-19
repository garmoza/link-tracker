package edu.java.bot.service;

import edu.java.bot.entity.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserById(long id);

    User saveUser(long userId);
}
