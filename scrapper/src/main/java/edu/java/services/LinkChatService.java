package edu.java.services;

import edu.java.domain.models.Chat;
import edu.java.domain.models.Link;
import java.util.List;

public interface LinkChatService {
    List<Link> listAllLinksByChatId(long chatId);

    List<Chat> listAllChatsByLinkId(long linkId);
}
