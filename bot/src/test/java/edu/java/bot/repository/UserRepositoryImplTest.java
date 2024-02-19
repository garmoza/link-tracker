package edu.java.bot.repository;

import edu.java.bot.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryImplTest {

    @Test
    void findUserById_UserNotFound() {
        var userRepository = new UserRepositoryImpl();
        userRepository.saveUser(new User(1L));

        Optional<User> savedUser = userRepository.findUserById(2L);

        assertThat(savedUser)
            .isEmpty();
    }

    @Test
    void saveUser() {
        var userRepository = new UserRepositoryImpl();
        userRepository.saveUser(new User(1L));

        Optional<User> savedUser = userRepository.findUserById(1L);

        assertThat(savedUser)
            .isNotEmpty();
    }
}
