package edu.java.scrapper.jdbc;

import edu.java.domain.dao.LinkDao;
import edu.java.domain.model.Link;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.OffsetDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class JdbcLinkDaoTests extends IntegrationTest {
    private static final String LINK_PATH_STRING = "https://github.com/kosandron/java-course-2023-backend";
    private static final OffsetDateTime TIME = OffsetDateTime.now();

    @Autowired
    private LinkDao linkDao;

    @Test
    @Transactional
    @Rollback
    void addNewLink() {
        Link link = Link.builder()
            .id(1L)
            .url(LINK_PATH_STRING)
            .lastCheckTime(TIME)
            .lastModifiedTime(TIME)
            .build();

        Link savedLink = linkDao.save(link);

        var links = linkDao.findAll();
        assertThat(links).contains(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void deleteExistedLink() {
        Link link = Link.builder()
            .id(1L)
            .url(LINK_PATH_STRING)
            .lastCheckTime(TIME)
            .lastModifiedTime(TIME)
            .build();

        Link savedLink = linkDao.save(link);

        linkDao.deleteById(savedLink.getId());

        var chats = linkDao.findAll();
        assertThat(chats).doesNotContain(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void deleteNotExistedLink() {
        long id = 1L;

        linkDao.deleteById(id);

        var chats = linkDao.findAll();
        assertThat(chats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findExistedLink() {
        Link link = Link.builder()
            .id(1L)
            .url(LINK_PATH_STRING)
            .lastCheckTime(TIME)
            .lastModifiedTime(TIME)
            .build();
        Link savedLink = linkDao.save(link);

        Optional<Link> foundLinkById = linkDao.findById(savedLink.getId());
        Optional<Link> foundLinkByUrl = linkDao.findByUrl(savedLink.getUrl());

        assertThat(foundLinkById).isPresent();
        assertThat(foundLinkByUrl).isPresent();
        assertThat(foundLinkById.get()).isEqualTo(foundLinkByUrl.get());
    }

    @Test
    @Transactional
    @Rollback
    void findNotExistedLink() {
        long id = 1L;

        Optional<Link> foundLinkById = linkDao.findById(id);
        Optional<Link> foundLinkByUrl = linkDao.findByUrl(LINK_PATH_STRING);
        assertThat(foundLinkById).isEmpty();
        assertThat(foundLinkByUrl).isEmpty();
    }
}
