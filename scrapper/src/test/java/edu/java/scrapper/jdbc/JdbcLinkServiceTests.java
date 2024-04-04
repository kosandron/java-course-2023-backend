package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.dao.LinkChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.model.Link;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "app.database-access-type=jdbc")
public class JdbcLinkServiceTests extends IntegrationTest {
    private static final String URL = "https://github.com/kosandron/java-course-2023-backend";

    @Autowired private LinkDao linkRepository;
    @Autowired private LinkChatDao linkChatRepository;
    @Autowired private ChatService chatService;
    @Autowired private LinkService linkService;

    @Test
    @Transactional
    @Rollback
    void registerNewLinkWithUnknownChatTest() {
        // Arrange
        long unknownChatId = 1L;

        // Assert
        assertThrows(
            ResourceNotFoundException.class,
            () -> linkService.add(unknownChatId, URL)
        );
    }

    @Test
    @Transactional
    @Rollback
    void registerNewLinkWithExistedChatTest() {
        // Arrange
        long chatId = 1L;
        chatService.register(chatId);

        // Act
        linkService.add(chatId, URL);

        // Assert
        assertThat(linkRepository.findByUrl(URL)).isPresent();
        assertThat(linkChatRepository.findAllLinksByChatId(chatId)).hasSize(1);
    }

    @Test
    @Transactional
    @Rollback
    void registerSameLinkInDifferentChatsTest() {
        // Arrange
        long chatId1 = 1L;
        long chatId2 = 2L;
        long chatId3 = 3L;
        chatService.register(chatId1);
        chatService.register(chatId2);
        chatService.register(chatId3);

        // Act
        linkService.add(chatId1, URL);
        linkService.add(chatId2, URL);
        linkService.add(chatId3, URL);

        // Assert
        assertThat(linkChatRepository.findAllLinksByChatId(chatId1)).hasSize(1);
        assertThat(linkChatRepository.findAllLinksByChatId(chatId2)).hasSize(1);
        assertThat(linkChatRepository.findAllLinksByChatId(chatId3)).hasSize(1);
        assertThat(linkRepository.findByUrl(URL)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    void removeNotTrackedLinkTest() {
        // Arrange
        long chatId = 1L;

        // Act, Assert
        assertThrows(
            ResourceNotFoundException.class,
            () -> linkService.remove(chatId, URL)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeTrackLinkThatTrackedByTwoChatsTest() {
        // Arrange
        long chatId1 = 1L;
        long chatId2 = 2L;
        chatService.register(chatId1);
        chatService.register(chatId2);
        linkService.add(chatId1, URL);
        linkService.add(chatId2, URL);

        // Act
        linkService.remove(chatId1, URL);

        // Assert
        assertThat(linkRepository.findByUrl(URL)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    void removeTrackLinkThatTrackedByTwoChatsTwiceByEachChatTest() {
        // Arrange
        long chatId1 = 1L;
        long chatId2 = 2L;
        chatService.register(chatId1);
        chatService.register(chatId2);
        linkService.add(chatId1, URL);
        linkService.add(chatId2, URL);

        // Act
        linkService.remove(chatId1, URL);
        linkService.remove(chatId2, URL);

        // Assert
        assertThat(linkChatRepository.findAllLinksByChatId(chatId1)).isEmpty();
        assertThat(linkChatRepository.findAllLinksByChatId(chatId2)).isEmpty();
        assertThat(linkRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void updateTimestamps() {
        // Arrange
        long chatId = 1L;
        chatService.register(chatId);
        linkService.add(chatId, URL);
        Link link = linkRepository.findByUrl(URL).get();
        OffsetDateTime newTime = OffsetDateTime.of(2024, 3, 24, 23, 15, 15, 0, ZoneOffset.UTC);

        // Act
        linkService.updateLastModified(link.getId(), newTime);
        linkService.updateLastChecked(link.getId(), newTime);

        // Assert
        Link updatedLink = linkRepository.findById(link.getId()).get();
        assertThat(updatedLink).isNotNull();
        assertThat(updatedLink.getLastCheckTime()).isEqualTo(newTime);
        assertThat(updatedLink.getLastModifiedTime()).isEqualTo(newTime);
    }
}
