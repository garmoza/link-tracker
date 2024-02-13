package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.mock.MockUpdateUtils;
import edu.java.bot.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCommandTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void command() {
        Command listCommand = new ListCommand(userRepository);

        String actualCommand = listCommand.command();

        assertEquals("/list", actualCommand);
    }

    @Test
    void description() {
        Command listCommand = new ListCommand(userRepository);

        String actualDescription = listCommand.description();

        assertEquals("show list of tracked links", actualDescription);
    }

    @Test
    void handle_UserIsNotRegistered() {
        when(userRepository.findUserById(2L)).thenReturn(Optional.empty());
        Command listCommand = new ListCommand(userRepository);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessage actualMessage = listCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "User is not registered.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_UserHasNoLinks() {
        when(userRepository.findUserById(2L)).thenReturn(Optional.of(new User(2L)));
        Command listCommand = new ListCommand(userRepository);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessage actualMessage = listCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, "There are no tracked links.");
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void handle_UserHasTrackingLinks() {
        User user = new User(2L);
        Set<String> links = user.getLinks();
        links.add("link1");
        links.add("link2");
        when(userRepository.findUserById(2L)).thenReturn(Optional.of(user));
        Command listCommand = new ListCommand(userRepository);

        Update updateMock = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessage actualMessage = listCommand.handle(updateMock);

        SendMessage expectedMessage = new SendMessage(1L, """
            Tracked links:
            - link1
            - link2
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
