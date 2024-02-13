package edu.java.bot.command;

import edu.java.bot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UntrackCommandTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void command() {
        Command untrackCommand = new UntrackCommand(userRepository);

        String actualCommand = untrackCommand.command();

        assertEquals("/untrack", actualCommand);
    }

    @Test
    void description() {
        Command untrackCommand = new UntrackCommand(userRepository);

        String actualDescription = untrackCommand.description();

        assertEquals("stop tracking link", actualDescription);
    }

    @Test
    void authorizedHandle() {
        // TODO
    }
}
