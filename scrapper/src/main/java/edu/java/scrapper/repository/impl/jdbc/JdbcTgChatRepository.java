package edu.java.scrapper.repository.impl.jdbc;

import edu.java.scrapper.entity.TgChat;
import edu.java.scrapper.repository.TgChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository implements TgChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public TgChat add(TgChat chat) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO tg_chat VALUES (?) RETURNING *",
            new BeanPropertyRowMapper<>(TgChat.class),
            chat.getId()
        );
    }

    @Override
    public Optional<TgChat> findById(long id) {
        try {
            TgChat chat = jdbcTemplate.queryForObject(
                "SELECT * FROM tg_chat WHERE id=?",
                new BeanPropertyRowMapper<>(TgChat.class),
                id
            );
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
    public List<TgChat> findAll() {
        return jdbcTemplate.query("SELECT * FROM tg_chat", new BeanPropertyRowMapper<>(TgChat.class));
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update("DELETE FROM tg_chat WHERE id=?", id);
    }
}
