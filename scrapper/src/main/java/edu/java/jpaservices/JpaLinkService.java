package edu.java.jpaservices;

import edu.java.domain.jpa.dao.JpaChatDao;
import edu.java.domain.jpa.dao.JpaLinksDao;
import edu.java.domain.jpa.model.Chat;
import edu.java.domain.jpa.model.Link;
import edu.java.dto.database.LinkDto;
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
    public LinkDto add(long chatId, String url) {
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

        return LinkDto.fromJpaLink(link);
    }

    @Override
    public LinkDto remove(long chatId, String url) {
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

        return LinkDto.fromJpaLink(link);
    }

    @Override
    public List<LinkDto> listByOldestCheck(int count) {
        return linkDao
            .findByOrderByLastCheckedAsc(Limit.of(count))
            .stream()
            .map(LinkDto::fromJpaLink)
            .toList();
    }

    @Override
    public List<LinkDto> listAll() {
        return linkDao
            .findAll()
            .stream()
            .map(LinkDto::fromJpaLink)
            .toList();
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
