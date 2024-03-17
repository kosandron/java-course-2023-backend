package edu.java.domain.dao;

import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import edu.java.domain.model.LinkChat;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkChatDao {
    private final JdbcTemplate jdbcTemplate;

    public LinkChat save(LinkChat linkChat) {
        jdbcTemplate.update(
            "INSERT INTO chats_links (link_id, chat_id) VALUES(?, ?) ON CONFLICT DO NOTHING",
            linkChat.getLinkId(), linkChat.getChatId()
        );
        return linkChat;
    }

    public int deleteByIds(long linkId, long chatId) {
        return jdbcTemplate.update(
            "DELETE FROM chats_links WHERE link_id = ? AND chat_id = ?",
            linkId, chatId
        );
    }

    public int deleteAllByChatId(long chatId) {
        return jdbcTemplate.update(
            "DELETE FROM chats_links WHERE chat_id = ?",
            chatId
        );
    }

    public int deleteAllByLinkId(long linkId) {
        return jdbcTemplate.update(
            "DELETE FROM chats_links WHERE linkId = ?",
            linkId
        );
    }

    public Collection<Link> findAllLinksByChatId(Long chatId) {
        return jdbcTemplate.query(
            "SELECT * FROM chats_links WHERE link_id IN (SELECT link_id FROM link_chat WHERE chat_id = ?)",
            (rs, rowNum) -> Link.parseResultSet(rs),
            chatId
        );
    }

    public Collection<Chat> findAllChatsByLinkId(Long linkId) {
        return jdbcTemplate.query(
            "SELECT chat_id FROM chats_links WHERE link_id = ?",
            (rs, rowNum) -> Chat.parseResultSet(rs),
            linkId
        );
    }
}
