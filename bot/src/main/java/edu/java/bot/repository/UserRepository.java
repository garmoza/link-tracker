package edu.java.bot.repository;

import edu.java.bot.entity.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findUserById(long id);

    void saveUser(User user);
}
