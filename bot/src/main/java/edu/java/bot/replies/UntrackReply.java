package edu.java.bot.replies;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repositories.LinkRepository;
import edu.java.bot.repositories.LinkValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UntrackReply implements Reply {
    private static final String REPLY = "What site do you want to end track?";
    public static final String SUCCESS = "Tracking of this resource ended!";
    public static final String FAILURE = "Cannot to end to track this resource!";
    private final LinkRepository linkRepository;

    @Override
    public String reply() {
        return REPLY;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String link = update.message().text();

        if (!LinkValidator.isValidLink(link)) {
            return new SendMessage(chatId, FAILURE);
        }

        linkRepository.untrackLink(chatId, link);
        return new SendMessage(chatId, SUCCESS);
    }
}
