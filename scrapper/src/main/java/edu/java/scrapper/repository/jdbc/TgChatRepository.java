package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.TgChat;
import java.util.List;
import java.util.Optional;

public interface TgChatRepository {

    TgChat add(TgChat chat);

    Optional<TgChat> findById(long id);

    boolean existsById(long id);

    List<TgChat> findAll();

    void remove(long id);
}
