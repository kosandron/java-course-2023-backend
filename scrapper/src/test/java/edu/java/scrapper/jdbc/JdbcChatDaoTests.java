package edu.java.scrapper.jdbc;

import edu.java.domain.dao.ChatDao;
import edu.java.domain.model.Chat;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.database-access-type=jdbc")
public class JdbcChatDaoTests extends IntegrationTest {
    @Autowired
    private ChatDao chatDao;

    @Test
    @Transactional
    @Rollback
    void addNewChatTest() {
        Chat chat = new Chat(1L);

        chatDao.save(chat);

        var chats = chatDao.findAll();
        assertThat(chats).contains(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteExistedChatTest() {
        long id = 1L;
        Chat chat = new Chat(id);

        chatDao.save(chat);
        chatDao.deleteById(id);

        var chats = chatDao.findAll();
        assertThat(chats).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteNotExistedChatTest() {
        long id = 1L;
        Chat chat = new Chat(id);

        chatDao.deleteById(id);

        var chats = chatDao.findAll();
        assertThat(chats).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdExistedChatTest() {
        long id = 1L;
        Chat chat = new Chat(id);

        chatDao.save(chat);

        Optional<Chat> foundChat = chatDao.findById(id);
        assertThat(foundChat).isPresent();
        assertThat(foundChat.get()).isEqualTo(chat);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdNotExistedChatTest() {
        long id = 1L;

        Optional<Chat> foundChat = chatDao.findById(id);
        assertThat(foundChat).isEmpty();
    }
}
