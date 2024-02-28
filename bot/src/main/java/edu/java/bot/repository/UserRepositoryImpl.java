package edu.java.bot.repository;

import edu.java.bot.entity.User;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> findUserById(long id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        return users.put(user.getId(), user);
    }
}
