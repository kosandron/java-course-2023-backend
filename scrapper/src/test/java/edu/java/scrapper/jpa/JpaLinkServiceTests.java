package edu.java.scrapper.jpa;


import edu.java.domain.dao.JpaLinksDao;
import edu.java.domain.models.Link;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.jpaservices.JpaChatService;
import edu.java.jpaservices.JpaLinkService;
import edu.java.scrapper.IntegrationTest;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "app.database-access-type=jpa")
@Testcontainers
public class JpaLinkServiceTests extends IntegrationTest {
    private static final String URL = "https://github.com/kosandron/java-course-2023-backend";

    @Autowired private JpaLinksDao linkRepository;
    @Autowired private JpaChatService chatService;
    @Autowired private JpaLinkService linkService;

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
        assertThat(linkRepository.findAllByChatsId(chatId)).hasSize(1);
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
        assertThat(linkRepository.findAllByChatsId(chatId1)).hasSize(1);
        assertThat(linkRepository.findAllByChatsId(chatId2)).hasSize(1);
        assertThat(linkRepository.findAllByChatsId(chatId3)).hasSize(1);
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
        assertThat(linkRepository.findAllByChatsId(chatId1)).isEmpty();
        assertThat(linkRepository.findAllByChatsId(chatId2)).isEmpty();
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
        assertThat(updatedLink.getLastChecked()).isEqualTo(newTime);
        assertThat(updatedLink.getLastModified()).isEqualTo(newTime);
    }
}
