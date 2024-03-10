package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repositories.LinkRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListCommand implements Command {
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "list of tracked site";
    private static final String EMPTY_LIST_REPLY = "You are tracking nothing";
    private static final String FILLED_LIST_REPLY = "List of tracked sites:";
    private final LinkRepository linkRepository;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        List<String> links = linkRepository.getTrackedLinks(update.message().chat().id());

        var strBuilder = new StringBuilder(
            links.isEmpty()
                ? EMPTY_LIST_REPLY
                : FILLED_LIST_REPLY
        );

        for (var link : links) {
            strBuilder.append('\n');
            strBuilder.append(link);
        }

        return new SendMessage(update.message().chat().id(), strBuilder.toString());
    }
}
