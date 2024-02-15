package edu.java.bot.service.impl;

import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.UserService;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserById(long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public User saveUser(long userId) {
        return userRepository.findUserById(userId)
            .orElse(userRepository.saveUser(new User(userId)));
    }
}
