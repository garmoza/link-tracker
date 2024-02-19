package edu.java.bot.entity;

import java.util.HashSet;
import lombok.Getter;

@Getter
public class User {

    private final long id;
    private final HashSet<Link> links;

    public User(long id) {
        this.id = id;
        this.links = new HashSet<>();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
