package edu.java.bot.repository;

import edu.java.bot.entity.User;
import java.util.HashMap;
import java.util.Optional;

public class UserRepository {

    private final HashMap<Long, User> users;

    public UserRepository() {
        users = new HashMap<>();
    }

    public Optional<User> findUserById(long id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    public void saveUser(User user) {
        users.put(user.getId(), user);
    }
}
