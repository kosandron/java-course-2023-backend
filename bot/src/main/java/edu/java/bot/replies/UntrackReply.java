package edu.java.bot.replies;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandConstants;
import edu.java.bot.repositories.LinkRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UntrackReply implements Reply {
    private final LinkRepository linkRepository;

    @Override
    public String reply() {
        return CommandConstants.UNTRACK_REPLY;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String link = update.message().text();

        if (!linkRepository.isValidLink(link)) {
            return new SendMessage(chatId, ReplyConstants.UNTRACK_FAILURE);
        }

        linkRepository.untrackLink(chatId, link);
        return new SendMessage(chatId, ReplyConstants.UNTRACK_SUCCESS);
    }
}
