package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "output of all commands description";
    private static final String SEPARATOR = " - ";

    private final String helpText;

    public HelpCommand(Command... commands) {
        var strBuilder = new StringBuilder();
        for (var command : commands) {
            strBuilder.append(command.command());
            strBuilder.append(SEPARATOR);
            strBuilder.append(command.description());
            strBuilder.append('\n');
        }
        strBuilder.append(COMMAND);
        strBuilder.append(SEPARATOR);
        strBuilder.append(DESCRIPTION);
        helpText = strBuilder.toString();
    }

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
        return new SendMessage(update.message().chat().id(), helpText);
    }
}
