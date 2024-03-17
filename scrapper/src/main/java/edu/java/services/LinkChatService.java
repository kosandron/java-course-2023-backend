package edu.java.services;

import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import java.util.List;

public interface LinkChatService {
    List<Link> listAllLinksByChatId(long tgChatId);

    List<Chat> listAllChatsByLinkId(long linkId);
}
