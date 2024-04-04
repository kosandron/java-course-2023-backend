package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.model.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.ChatService;
import edu.java.services.LinkChatService;
import edu.java.services.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "app.database-access-type=jdbc")
public class JdbcChatServiceTests extends IntegrationTest {
    @Autowired private ChatDao chatRepository;
    @Autowired private ChatService chatService;
    @Autowired private LinkService linkService;
    @Autowired private LinkChatService linkChatService;

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
        assertThat(chats).anyMatch(chat -> chat.getChatId().equals(chatId));
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
        assertThat(chatRepository.findAll()).doesNotContain(new Chat(chatId));
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
