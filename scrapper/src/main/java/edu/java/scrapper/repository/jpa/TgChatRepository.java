package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.TgChat;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgChatRepository extends ListCrudRepository<TgChat, Long> {
}
