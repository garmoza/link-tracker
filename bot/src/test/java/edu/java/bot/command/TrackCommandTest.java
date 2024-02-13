package edu.java.bot.command;

import edu.java.bot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TrackCommandTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void command() {
        Command trackCommand = new TrackCommand(userRepository);

        String actualCommand = trackCommand.command();

        assertEquals("/track", actualCommand);
    }

    @Test
    void description() {
        Command trackCommand = new TrackCommand(userRepository);

        String actualDescription = trackCommand.description();

        assertEquals("start tracking link", actualDescription);
    }

    @Test
    void authorizedHandle() {
        // TODO
    }
}
