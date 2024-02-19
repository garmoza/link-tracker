package edu.java.bot.util;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.entity.Link;
import edu.java.bot.mock.MockCommandUtils;
import edu.java.bot.mock.MockUpdateUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SendMessageFormatterTest {

    @Test
    void isEmpty() {
        Update update = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessageFormatter formatter = new SendMessageFormatter(update);

        boolean isEmpty = formatter.isEmpty();

        assertThat(isEmpty).as("checks that formatter is empty after creation").isTrue();
    }

    @Test
    void append_FormattedString() {
        Update update = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessageFormatter formatter = new SendMessageFormatter(update);

        formatter.append("before %s after", "value");
        SendMessage actualMessage = formatter.getMessage();

        SendMessage expectedMessage = new SendMessage(1L, "before value after")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void append_SimpleString() {
        Update update = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessageFormatter formatter = new SendMessageFormatter(update);

        formatter.append("test");
        SendMessage actualMessage = formatter.getMessage();

        SendMessage expectedMessage = new SendMessage(1L, "test")
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void appendHandlerList() {
        CommandHandler command1 = MockCommandUtils.getCommandMock("/command", "description of command");
        CommandHandler command2 = MockCommandUtils.getCommandMock("/another", "another description");

        Update update = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessageFormatter formatter = new SendMessageFormatter(update);
        formatter.appendHandlerList(List.of(command1, command2));
        SendMessage actualMessage = formatter.getMessage();

        SendMessage expectedMessage = new SendMessage(1L, """
            */command* - description of command
            */another* - another description
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }

    @Test
    void appendLinkList() {
        Link link1 = new Link("url1", "host1");
        Link link2 = new Link("url2", "host2");

        Update update = MockUpdateUtils.getUpdateMock(1L, 2L);
        SendMessageFormatter formatter = new SendMessageFormatter(update);
        formatter.appendLinkList(List.of(link1, link2));
        SendMessage actualMessage = formatter.getMessage();

        SendMessage expectedMessage = new SendMessage(1L, """
            - url1
            - url2
            """)
            .parseMode(ParseMode.Markdown);
        assertEquals(expectedMessage.getParameters(), actualMessage.getParameters());
    }
}
