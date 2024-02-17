package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repositories.LinkRepository;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListCommand implements Command {
    private final LinkRepository linkRepository;

    @Override
    public String command() {
        return CommandConstants.LIST_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.LIST_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        List<String> links = linkRepository.getTrackedLinks(update.message().chat().id());

        var strBuilder = new StringBuilder(
            links.isEmpty()
                ? CommandConstants.EMPTY_LIST_REPLY
                : CommandConstants.FILLED_LIST_REPLY
        );

        for (var link : links) {
            strBuilder.append('\n');
            strBuilder.append(link);
        }

        return new SendMessage(update.message().chat().id(), strBuilder.toString());
    }
}
