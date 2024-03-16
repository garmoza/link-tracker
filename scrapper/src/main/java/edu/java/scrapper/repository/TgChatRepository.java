package edu.java.scrapper.repository;

import edu.java.scrapper.entity.TgChat;
import java.util.Optional;

public interface TgChatRepository {

    TgChat add(TgChat chat);

    Optional<TgChat> findById(long id);

    boolean existsById(long id);

    Iterable<TgChat> findAll();

    void remove(long id);
}
