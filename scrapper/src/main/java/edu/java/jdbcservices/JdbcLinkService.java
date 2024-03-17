package edu.java.jdbcservices;

import edu.java.domain.dao.ChatDao;
import edu.java.domain.dao.LinkChatDao;
import edu.java.domain.dao.LinkDao;
import edu.java.domain.model.Link;
import edu.java.domain.model.LinkChat;
import edu.java.exception.ResourceNotFoundException;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final ChatDao chatDao;
    private final LinkDao linkDao;
    private final LinkChatDao linkChatDao;

    @Override
    public Link add(long chatId, String url) {
        if (chatDao.findById(chatId).isEmpty()) {
            throw new ResourceNotFoundException();
        }

        Optional<Link> optionalLink = linkDao.findByUrl(url);
        Link link = optionalLink.orElseGet(() -> linkDao.save(Link.builder().url(url).build()));

        linkChatDao.save(
            LinkChat.builder()
                .linkId(link.getId())
                .chatId(chatId)
                .build()
        );

        return link;
    }

    @Override
    public Link remove(long chatId, String url) {
        Link link = linkDao.findByUrl(url)
            .orElseThrow(() -> new ResourceNotFoundException());
        linkChatDao.deleteByIds(link.getId(), chatId);

        if (linkChatDao.findAllChatsByLinkId(link.getId()).isEmpty()) {
            linkDao.deleteById(link.getId());
        }

        return link;
    }

    @Override
    public List<Link> listByOldestCheck(int count) {
        return linkDao.findNLinksByOldestLastCheck(count)
            .stream()
            .toList();
    }

    @Override
    public List<Link> listAll() {
        return linkDao.findAll()
            .stream()
            .toList();
    }

    @Override
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        Link link = linkDao.findById(linkId)
            .orElseThrow(() -> new ResourceNotFoundException());

        link.setLastModifiedTime(offsetDateTime);
        linkDao.updateLink(link);
    }

    @Override
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        Link link = linkDao.findById(linkId)
            .orElseThrow(() -> new ResourceNotFoundException());

        link.setLastCheckTime(offsetDateTime);
        linkDao.updateLink(link);
    }
}
