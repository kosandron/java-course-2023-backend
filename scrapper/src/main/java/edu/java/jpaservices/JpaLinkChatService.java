package edu.java.jpaservices;

import edu.java.domain.jpa.dao.JpaChatDao;
import edu.java.domain.jpa.dao.JpaLinksDao;
import edu.java.dto.database.ChatDto;
import edu.java.dto.database.LinkDto;
import edu.java.services.LinkChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaLinkChatService implements LinkChatService {
    private final JpaChatDao chatDao;
    private final JpaLinksDao linkDao;

    @Override
    public List<LinkDto> listAllLinksByChatId(long chatId) {
        return linkDao
            .findAllByChatsId(chatId)
            .stream()
            .map(LinkDto::fromJpaLink)
            .toList();
    }

    @Override
    public List<ChatDto> listAllChatsByLinkId(long linkId) {
        return chatDao
            .findAllByLinksId(linkId)
            .stream()
            .map(ChatDto::fromJpaChat)
            .toList();
    }
}
