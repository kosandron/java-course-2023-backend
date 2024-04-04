package edu.java.jdbcservices;

import edu.java.domain.dao.ChatDao;
import edu.java.domain.dao.LinkChatDao;
import edu.java.domain.dao.LinkDao;
import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.ResourceNotFoundException;
import edu.java.services.ChatService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final LinkDao linkDao;
    private final ChatDao chatDao;
    private final LinkChatDao linkChatDao;

    @Override
    public void register(long chatId) {
        if (chatDao.findById(chatId).isPresent()) {
            throw new ChatAlreadyExistsException();
        }

        chatDao.save(new Chat(chatId));
    }

    @Override
    public void unregister(long chatId) {
        if (chatDao.findById(chatId).isEmpty()) {
            throw new ResourceNotFoundException();
        }

        Collection<Link> links = linkChatDao.findAllLinksByChatId(chatId);
        linkChatDao.deleteAllByChatId(chatId);
        for (var link : links) {
            long linkId = link.getId();
            if (linkChatDao.findAllChatsByLinkId(linkId).isEmpty()) {
                linkDao.deleteById(linkId);
            }
        }

        chatDao.deleteById(chatId);
    }
}
