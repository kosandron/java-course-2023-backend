package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatDao {
    private final JdbcTemplate jdbcTemplate;

    public Chat save(Chat chat) {
        int affectedRows =
            jdbcTemplate.update("INSERT INTO chats VALUES (?) ON CONFLICT DO NOTHING", chat.getChatId());
        if (affectedRows == 0) {
            throw new ChatAlreadyExistsException();
        }

        return chat;
    }

    public Chat deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM chats WHERE id = ?", id);
        return Chat.builder().chatId(id).build();
    }

    public Optional<Chat> findById(long chatId) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM chats WHERE id = ?",
                    (rs, rowNum) -> Chat.parseResultSet(rs),
                    chatId
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Collection<Chat> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chats",
            (rs, rowNum) -> Chat.parseResultSet(rs)
        );
    }
}
