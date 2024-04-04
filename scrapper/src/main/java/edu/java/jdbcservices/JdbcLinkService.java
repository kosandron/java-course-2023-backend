package edu.java.jdbcservices;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.dao.LinkChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.domain.jdbc.model.Link;
import edu.java.domain.jdbc.model.LinkChat;
import edu.java.dto.database.LinkDto;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.LinkService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    public LinkDto add(long chatId, String url) {
        if (chatDao.findById(chatId).isEmpty()) {
            throw ResourceNotFoundException.chatNotFoundException(chatId);
        }

        Optional<Link> optionalLink = linkDao.findByUrl(url);
        Link link = optionalLink.orElseGet(() -> linkDao.save(Link.builder().url(url)
            .lastCheckTime(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC))
            .lastModifiedTime(OffsetDateTime.now())
            .build()));

        linkChatDao.save(
            LinkChat.builder()
                .linkId(link.getId())
                .chatId(chatId)
                .build()
        );

        return LinkDto.fromJdbcLink(link);
    }

    @Override
    public LinkDto remove(long chatId, String url) {
        Link link = linkDao.findByUrl(url)
            .orElseThrow(ResourceNotFoundException::new);
        linkChatDao.deleteByIds(link.getId(), chatId);

        if (linkChatDao.findAllChatsByLinkId(link.getId()).isEmpty()) {
            linkDao.deleteById(link.getId());
        }

        return LinkDto.fromJdbcLink(link);
    }

    @Override
    public List<LinkDto> listByOldestCheck(int count) {
        return linkDao.findNLinksByOldestLastCheck(count)
            .stream()
            .map(LinkDto::fromJdbcLink)
            .toList();
    }

    @Override
    public List<LinkDto> listAll() {
        return linkDao.findAll()
            .stream()
            .map(LinkDto::fromJdbcLink)
            .toList();
    }

    @Override
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        Link link = linkDao.findById(linkId)
            .orElseThrow(ResourceNotFoundException::new);

        link.setLastModifiedTime(offsetDateTime);
        linkDao.updateLink(link);
    }

    @Override
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        Link link = linkDao.findById(linkId)
            .orElseThrow(ResourceNotFoundException::new);

        link.setLastCheckTime(offsetDateTime);
        linkDao.updateLink(link);
    }
}
