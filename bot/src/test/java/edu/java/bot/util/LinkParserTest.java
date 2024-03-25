package edu.java.bot.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkParserTest {

    @Test
    void parse() throws URLParseException {
        String link = LinkParser.parse("https://github.com/sanyarnd/tinkoff-java-course-2023/");

        assertEquals("https://github.com/sanyarnd/tinkoff-java-course-2023/", link);
    }

    @Test
    void parse_NotValid1() {
        assertThrows(URLParseException.class, () -> LinkParser.parse("github.com/sanyarnd/tinkoff-java-course-2023/"));
    }

    @Test
    void parse_NotValid2() {
        assertThrows(
            URLParseException.class,
            () -> LinkParser.parse("hts://github.com/sanyarnd/tinkoff-java-course-2023/")
        );
    }
}
