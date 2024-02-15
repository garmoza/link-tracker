package edu.java.bot.service;

import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(long userId) {
        return userRepository.findUserById(userId)
            .orElse(userRepository.saveUser(new User(userId)));
    }
}
