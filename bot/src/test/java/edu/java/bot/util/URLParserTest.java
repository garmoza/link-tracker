package edu.java.bot.util;

import java.net.URL;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class URLParserTest {

    @Test
    void parse_CheckHost() {
        URL url = URLParser.parse("https://github.com/sanyarnd/tinkoff-java-course-2023/");

        assertEquals("github.com", url.getHost());
    }

    @Test
    void parse_NotValid1() {
        assertThrows(URLParseException.class, () -> URLParser.parse("github.com/sanyarnd/tinkoff-java-course-2023/"));
    }

    @Test
    void parse_NotValid2() {
        assertThrows(
            URLParseException.class,
            () -> URLParser.parse("hts://github.com/sanyarnd/tinkoff-java-course-2023/")
        );
    }
}
