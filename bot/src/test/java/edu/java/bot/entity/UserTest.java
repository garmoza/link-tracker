package edu.java.bot.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void getId() {
        User user = new User(1L);

        assertEquals(1L, user.getId());
    }

    @Test
    void getLinks() {
        User user = new User(1L);

        assertThat(user.getLinks())
            .isNotNull()
            .isEmpty();
    }
}
