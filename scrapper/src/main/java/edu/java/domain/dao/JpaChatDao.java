package edu.java.domain.dao;

import edu.java.domain.models.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatDao extends JpaRepository<Chat, Long> {
    List<Chat> findAllByLinksId(Long id);
}
