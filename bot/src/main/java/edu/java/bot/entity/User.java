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
}
