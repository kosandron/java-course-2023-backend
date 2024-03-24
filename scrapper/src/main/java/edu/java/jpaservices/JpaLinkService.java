package edu.java.jpaservices;

import edu.java.domain.dao.JpaChatDao;
import edu.java.domain.dao.JpaLinksDao;
import edu.java.domain.models.Chat;
import edu.java.domain.models.Link;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.LinkService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaChatDao chatDao;
    private final JpaLinksDao linkDao;

    @Override
    public Link add(long chatId, String url) {
        Optional<Chat> chatOptional = chatDao.findById(chatId);
        if (chatOptional.isEmpty()) {
            throw ResourceNotFoundException.chatNotFoundException(chatId);
        }

        Link link = linkDao.findByUrl(url)
            .orElseGet(() -> linkDao.saveAndFlush(
                Link.builder()
                    .url(url)
                    .lastChecked(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC))
                    .lastModified(OffsetDateTime.now())
                    .build())
            );

        linkDao.insertLinkChat(link.getId(), chatId);

        return link;
    }

    @Override
    public Link remove(long chatId, String url) {
        Optional<Chat> chatOptional = chatDao.findById(chatId);
        if (chatOptional.isEmpty()) {
            throw ResourceNotFoundException.chatNotFoundException(chatId);
        }

        Link link = linkDao.findByUrl(url)
            .orElseThrow(() ->
                new ResourceNotFoundException("Chat with id %d does not contains link id %s!".formatted(chatId, url)));

        Long linkId = link.getId();
        linkDao.removeLinkChat(linkId, chatId);
        if (chatDao.findAllByLinksId(linkId).isEmpty()) {
            linkDao.deleteById(linkId);
        }

        return link;
    }

    @Override
    public List<Link> listByOldestCheck(int count) {
        return linkDao.findByOrderByLastCheckedAsc(Limit.of(count));
    }

    @Override
    public List<Link> listAll() {
        return linkDao.findAll();
    }

    @Override
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        Optional<Link> linkOptional = linkDao.findById(linkId);
        if (linkOptional.isEmpty()) {
            throw ResourceNotFoundException.linkNotFoundException(linkId);
        }

        Link link = linkOptional.get();
        link.setLastModified(offsetDateTime);
        linkDao.save(link);
    }

    @Override
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        Optional<Link> linkOptional = linkDao.findById(linkId);
        if (linkOptional.isEmpty()) {
            throw ResourceNotFoundException.linkNotFoundException(linkId);
        }

        Link link = linkOptional.get();
        link.setLastChecked(offsetDateTime);
        linkDao.save(link);
    }
}
