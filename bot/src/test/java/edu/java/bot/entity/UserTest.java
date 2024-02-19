package edu.java.bot.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void equals_ById_ReturnsTrue() {
        User user1 = new User(2L);
        User user2 = new User(2L);

        boolean isEquals = user1.equals(user2);

        assertThat(isEquals).as("checks that users entities equals by id").isTrue();
    }

    @Test
    void equals_ById_ReturnsFalse() {
        User user1 = new User(1L);
        User user2 = new User(2L);

        boolean isEquals = user1.equals(user2);

        assertThat(isEquals).as("checks that users entities is not equals by id").isFalse();
    }

    @Test
    void equals_WithSelfRef() {
        User user = new User(1L);

        boolean isEquals = user.equals(user);

        assertThat(isEquals).as("checks that true is returned when compared with self ref").isTrue();
    }

    @Test
    void equals_WithNull() {
        User user = new User(1L);

        boolean isEquals = user.equals(null);

        assertThat(isEquals).as("checks that false is returned when compared with null").isFalse();
    }

    @Test
    void hashCode_ContractWithEquals() {
        User user1 = new User(2L);
        User user2 = new User(2L);

        boolean isEquals = user1.equals(user2);

        assertThat(isEquals).isTrue();
        assertThat(user1.hashCode() == user2.hashCode()).as(
            "checks that if two objects are equals, then hashCode is the same");
    }
}
