package edu.java.bot.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LinkTest {

    @Test
    void equals_ByUrl_ReturnsTrue() {
        Link link1 = new Link("url", "host");
        Link link2 = new Link("url", "host");

        boolean isEquals = link1.equals(link2);

        assertThat(isEquals).as("checks that links entities equals by url").isTrue();
    }

    @Test
    void equals_ByUrl_ReturnsFalse() {
        Link link1 = new Link("url1", "host");
        Link link2 = new Link("url2", "host");

        boolean isEquals = link1.equals(link2);

        assertThat(isEquals).as("checks that links entities is not equals by url").isFalse();
    }

    @Test
    void equals_WithSelfRef() {
        Link link = new Link("url", "host");

        boolean isEquals = link.equals(link);

        assertThat(isEquals).as("checks that true is returned when compared with self ref").isTrue();
    }

    @Test
    void equals_WithNull() {
        Link link = new Link("url", "host");

        boolean isEquals = link.equals(null);

        assertThat(isEquals).as("checks that false is returned when compared with null").isFalse();
    }

    @Test
    void hashCode_ContractWithEquals() {
        Link link1 = new Link("url", "host");
        Link link2 = new Link("url", "host");

        boolean isEquals = link1.equals(link2);

        assertThat(isEquals).isTrue();
        assertThat(link1.hashCode() == link2.hashCode()).as(
            "checks that if two objects are equals, then hashCode is the same");
    }
}
