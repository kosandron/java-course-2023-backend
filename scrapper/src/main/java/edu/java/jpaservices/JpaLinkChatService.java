package edu.java.jpaservices;

import edu.java.domain.dao.JpaChatDao;
import edu.java.domain.dao.JpaLinksDao;
import edu.java.domain.models.Chat;
import edu.java.domain.models.Link;
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
    public List<Link> listAllLinksByChatId(long chatId) {
        return linkDao.findAllByChatsId(chatId);
    }

    @Override
    public List<Chat> listAllChatsByLinkId(long linkId) {
        return chatDao.findAllByLinksId(linkId);
    }
}
