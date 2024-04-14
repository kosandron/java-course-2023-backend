package edu.java.services;

import edu.java.dto.database.ChatDto;
import edu.java.dto.database.LinkDto;
import java.util.List;

public interface LinkChatService {
    List<LinkDto> listAllLinksByChatId(long chatId);

    List<ChatDto> listAllChatsByLinkId(long linkId);
}
