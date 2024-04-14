package edu.java.domain.jpa.dao;

import edu.java.domain.jpa.model.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatDao extends JpaRepository<Chat, Long> {
    List<Chat> findAllByLinksId(Long id);
}
