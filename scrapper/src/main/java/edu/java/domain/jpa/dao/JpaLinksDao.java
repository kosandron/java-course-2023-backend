package edu.java.domain.jpa.dao;

import edu.java.domain.jpa.model.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinksDao extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    List<Link> findAllByChatsId(Long chatId);

    List<Link> findByOrderByLastCheckedAsc(Limit limit);

    @Modifying
    @Query(value = "DELETE FROM chats_links WHERE chat_id = :chatId", nativeQuery = true)
    void deleteAllFromLinkChatsByChatId(Long chatId);

    @Modifying
    @Query(value = "INSERT INTO chats_links (link_id, chat_id) VALUES (:linkId, :chatId)", nativeQuery = true)
    void insertLinkChat(Long linkId, Long chatId);

    @Modifying
    @Query(value = "DELETE FROM chats_links WHERE link_id = :linkId AND chat_id = :chatId", nativeQuery = true)
    void removeLinkChat(Long linkId, Long chatId);
}
