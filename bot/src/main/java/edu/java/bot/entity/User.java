package edu.java.bot.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;

@Getter
@Setter
public class User {

    private long id;
    private HashSet<String> links;

    public User(long id) {
        this.id = id;
        this.links = new HashSet<>();
    }
}
