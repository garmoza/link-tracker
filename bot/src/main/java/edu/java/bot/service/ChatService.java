package edu.java.bot.service;

public interface ChatService {

    boolean existsById(long id);

    void registerChat(long id);
}
