package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.TgChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgChatRepository extends CrudRepository<TgChat, Long> {
}
