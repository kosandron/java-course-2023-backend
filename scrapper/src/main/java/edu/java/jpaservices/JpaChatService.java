package edu.java.jpaservices;

import edu.java.domain.dao.JpaChatDao;
import edu.java.domain.dao.JpaLinksDao;
import edu.java.domain.models.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.ChatService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatDao chatDao;
    private final JpaLinksDao linkDao;

    @Override
    public void register(long chatId) {
        if (chatDao.findById(chatId).isPresent()) {
            throw new ChatAlreadyExistsException();
        }

        chatDao.save(new Chat(chatId, new ArrayList<>()));
    }

    @Override
    public void unregister(long chatId) {
        if (chatDao.findById(chatId).isEmpty()) {
            throw new ResourceNotFoundException("Chat with id %d was not found!".formatted(chatId));
        }

        linkDao.deleteAllFromLinkChatsByChatId(chatId);
        chatDao.deleteById(chatId);
    }
}
