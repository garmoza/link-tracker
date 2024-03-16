package edu.java.scrapper.repository.impl.mapper;

import edu.java.scrapper.entity.TgChat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TgChatRowMapper implements RowMapper<TgChat> {

    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        TgChat chat = new TgChat();

        chat.setId(rs.getInt("id"));

        return chat;
    }
}
