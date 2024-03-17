package edu.java.jdbcservices;

import edu.java.domain.dao.LinkChatDao;
import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import edu.java.services.LinkChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcLinkChatService implements LinkChatService {
    private final LinkChatDao linkChatDao;

    @Override
    public List<Link> listAllLinksByChatId(long chatId) {
        return linkChatDao.findAllLinksByChatId(chatId)
            .stream()
            .toList();
    }

    @Override
    public List<Chat> listAllChatsByLinkId(long linkId) {
        return linkChatDao.findAllChatsByLinkId(linkId)
            .stream()
            .toList();
    }
}
