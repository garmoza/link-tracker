package edu.java.scrapper.repository.impl;

import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.repository.TgChatRepository;
import edu.java.scrapper.repository.impl.mapper.TgChatRowMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository implements TgChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TgChatRowMapper tgChatRowMapper;

    @Override
    public TgChat add(TgChat chat) {
        jdbcTemplate.update("INSERT INTO tg_chat VALUES (?)", chat.getId());
        return chat;
    }

    @Override
    public Optional<TgChat> findById(long id) {
        try {
            TgChat chat = jdbcTemplate.queryForObject("SELECT * FROM tg_chat WHERE id=?", tgChatRowMapper, id);
            return Optional.ofNullable(chat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(long id) {
        return findById(id).isPresent();
    }

    @Override
    public Iterable<TgChat> findAll() {
        return jdbcTemplate.query("SELECT * FROM tg_chat", tgChatRowMapper);
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update("DELETE FROM tg_chat WHERE id = ?", id);
    }
}
