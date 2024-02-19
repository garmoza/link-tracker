package edu.java.bot.service.impl;

import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserById_UserFound() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(new User(1L)));

        Optional<User> actualUser = userService.findUserById(1L);

        assertThat(actualUser)
            .isNotEmpty();
    }

    @Test
    void findUserById_UserNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findUserById(1L);

        assertThat(actualUser)
            .isEmpty();
    }

    @Test
    void saveUser_SaveNewUser() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(new User(1L));

        User actualUser = userService.saveUser(1L);

        assertEquals(new User(1L), actualUser);
    }

    @Test
    void saveUser_UserAlreadyExist() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(new User(1L)));

        User actualUser = userService.saveUser(1L);

        assertEquals(new User(1L), actualUser);
    }
}
