package edu.java.bot.repository;

import edu.java.bot.entity.User;
import java.util.HashMap;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> users;

    public UserRepositoryImpl() {
        users = new HashMap<>();
    }

    @Override
    public Optional<User> findUserById(long id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        return users.put(user.getId(), user);
    }
}
