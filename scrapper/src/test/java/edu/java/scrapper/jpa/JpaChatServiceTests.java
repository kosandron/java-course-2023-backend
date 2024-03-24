package edu.java.scrapper.jpa;

import edu.java.domain.dao.JpaChatDao;
import edu.java.domain.models.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.jpaservices.JpaChatService;
import edu.java.jpaservices.JpaLinkChatService;
import edu.java.jpaservices.JpaLinkService;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "app.database-access-type=jpa")
@Testcontainers
public class JpaChatServiceTests extends IntegrationTest {
    @Autowired private JpaChatDao chatRepository;
    @Autowired private JpaChatService chatService;
    @Autowired private JpaLinkService linkService;
    @Autowired private JpaLinkChatService linkChatService;

    @Test
    @Transactional
    @Rollback
    void registerChatTest() {
        // Arrange
        long chatId = 1L;
        chatService.register(chatId);

        // Act
        var chats = chatRepository.findAll();

        // Assert
        assertThat(chats).anyMatch(chat -> chat.getId().equals(chatId));
    }

    @Test
    @Transactional
    @Rollback
    void registerExistedChatTest() {
        // Arrange
        long chatId = 1L;
        chatService.register(chatId);

        // Act, Assert
        assertThrows(ChatAlreadyExistsException.class, () -> chatService.register(chatId));
    }

    @Test
    @Transactional
    @Rollback
    void unregisterExistedChatTest() {
        // Arrange
        long chatId = 1L;

        // Act
        chatService.register(chatId);
        chatService.unregister(chatId);

        // Assert
        assertThat(chatRepository.findAll()).doesNotContain(new Chat(chatId, new ArrayList<>()));
    }

    @Test
    @Transactional
    @Rollback
    void unregisterExistedChatAndRemoveTrackedLinksWithHimTest() {
        // Arrange
        long chatId = 1L;
        String url = "https://github.com/kosandron/java-course-2023-backend";
        chatService.register(chatId);
        linkService.add(chatId, url);

        // Act
        chatService.unregister(chatId);

        // Assert
        assertThat(chatRepository.findAll()).isEmpty();
        assertThat(linkChatService.listAllLinksByChatId(chatId)).isEmpty();
    }
}
