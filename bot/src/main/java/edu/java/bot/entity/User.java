package edu.java.bot.entity;

import java.util.HashSet;
import lombok.Getter;

@Getter
public class User {

    private long id;
    private HashSet<String> links;

    public User(long id) {
        this.id = id;
        this.links = new HashSet<>();
    }
}
