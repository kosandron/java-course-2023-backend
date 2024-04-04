package edu.java.jdbcservices;

import edu.java.domain.jdbc.dao.LinkChatDao;
import edu.java.dto.database.ChatDto;
import edu.java.dto.database.LinkDto;
import edu.java.services.LinkChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcLinkChatService implements LinkChatService {
    private final LinkChatDao linkChatDao;

    @Override
    public List<LinkDto> listAllLinksByChatId(long chatId) {
        return linkChatDao.findAllLinksByChatId(chatId)
            .stream()
            .map(LinkDto::fromJdbcLink)
            .toList();
    }

    @Override
    public List<ChatDto> listAllChatsByLinkId(long linkId) {
        return linkChatDao.findAllChatsByLinkId(linkId)
            .stream()
            .map(ChatDto::fromJdbcChat)
            .toList();
    }
}
